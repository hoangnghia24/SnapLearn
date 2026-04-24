package hcmute.edu.vn.snaplearn.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.viewmodels.ScannerViewModel;

public class ScannerActivity extends AppCompatActivity {

    private PreviewView viewFinder;
    private Button btnCapture;
    private ProgressBar progressBar;
    private ImageCapture imageCapture; // Thay ImageAnalysis bằng ImageCapture
    private ExecutorService cameraExecutor;
    private TextRecognizer textRecognizer;
    private ScannerViewModel scannerViewModel;
    private static final int CAMERA_PERMISSION_CODE = 100;

    // Trình hứng kết quả từ màn hình Cắt ảnh (Crop)
    private final ActivityResultLauncher<CropImageContractOptions> cropImageLauncher =
            registerForActivityResult(new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    Uri croppedUri = result.getUriContent();
                    if (croppedUri != null) {
                        processCroppedImage(croppedUri); // Cắt xong thì mang đi nhận diện chữ
                    }
                } else {
                    // Người dùng bấm Hủy cắt ảnh, mở lại nút Chụp
                    btnCapture.setEnabled(true);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        initViews();
        setupViewModel();

        cameraExecutor = Executors.newSingleThreadExecutor();
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        btnCapture.setOnClickListener(v -> takePhoto());
    }

    private void initViews() {
        viewFinder = findViewById(R.id.viewFinder);
        btnCapture = findViewById(R.id.btnCapture);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupViewModel() {
        scannerViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);

        scannerViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnCapture.setEnabled(!isLoading);
        });

        scannerViewModel.getScanResult().observe(this, result -> {
            if (result != null) {
                Intent intent = new Intent();
                intent.putExtra("SCANNED_WORD_EN", result[0]);
                intent.putExtra("TRANSLATED_WORD_VI", result[1]);
                intent.putExtra("PHONETIC_TEXT", result[2]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        scannerViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                btnCapture.setEnabled(true);
            }
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                // Khởi tạo ImageCapture thay vì ImageAnalysis
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraX", "Lỗi khởi tạo Camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    // --- BƯỚC 1: CHỤP ẢNH ---
    private void takePhoto() {
        if (imageCapture == null) return;
        btnCapture.setEnabled(false);

        // Tạo file tạm trong cache để lưu ảnh
        File photoFile = new File(getCacheDir(), "temp_snap.jpg");
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri savedUri = Uri.fromFile(photoFile);
                        launchImageCropper(savedUri); // Chụp xong chuyển sang màn hình Cắt
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(ScannerActivity.this, "Lỗi chụp ảnh!", Toast.LENGTH_SHORT).show();
                        btnCapture.setEnabled(true);
                    }
                });
    }

    // --- BƯỚC 2: CẮT ẢNH ---
    private void launchImageCropper(Uri uri) {
        CropImageOptions options = new CropImageOptions();

        // 1. Tắt các tùy chọn không cần thiết
        options.imageSourceIncludeGallery = false;
        options.imageSourceIncludeCamera = false;
        options.guidelines = com.canhub.cropper.CropImageView.Guidelines.ON;

        // 2. GÁN TRỰC TIẾP GIÁ TRỊ (Không dùng chữ "set" ở đầu)
        // Thu nhỏ khung cắt vào 15% để tránh dính lên cụm camera (tai thỏ)
        options.initialCropWindowPaddingRatio = 0.15f;

        // Sửa lỗi ở đây: Đổi từ toolbarTitle thành activityTitle
        options.activityTitle = "Kéo khung chọn từ vựng";

        // 3. Khởi chạy màn hình cắt
        CropImageContractOptions contractOptions = new CropImageContractOptions(uri, options);
        cropImageLauncher.launch(contractOptions);
    }

    // --- BƯỚC 3: NHẬN DIỆN CHỮ TỪ ẢNH ĐÃ CẮT ---
    private void processCroppedImage(Uri croppedUri) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            InputImage image = InputImage.fromFilePath(this, croppedUri);
            textRecognizer.process(image)
                    .addOnSuccessListener(visionText -> {
                        String text = visionText.getText();
                        if (!text.trim().isEmpty()) {

                            // SỬA LẠI TẠI ĐÂY:
                            // Xóa split, chỉ dùng trim() để xóa khoảng trắng thừa ở 2 đầu
                            // Thêm replace("\n", " ") để nối các chữ bị rớt dòng thành 1 câu ngang
                            String targetWord = text.trim().replace("\n", " ");

                            // Giao cho ViewModel tiếp tục luồng dịch & lấy API
                            scannerViewModel.processScannedWord(targetWord);

                        } else {
                            Toast.makeText(this, "Không tìm thấy chữ trong vùng đã cắt!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btnCapture.setEnabled(true);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi đọc chữ ML Kit!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        btnCapture.setEnabled(true);
                    });
        } catch (IOException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
            btnCapture.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textRecognizer != null) textRecognizer.close();
        cameraExecutor.shutdown();
    }
}