package hcmute.edu.vn.snaplearn.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.concurrent.ExecutionException;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.viewmodels.ScannerViewModel;

@ExperimentalGetImage
public class ScannerActivity extends AppCompatActivity {

    private PreviewView viewFinder;
    private TextView tvScannedText;
    private Button btnCapture;
    private ProgressBar progressBar;

    private String currentScannedText = "";
    private TextRecognizer textRecognizer;

    // Khai báo ViewModel
    private ScannerViewModel scannerViewModel;

    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        initViews();
        setupViewModel();

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        listener();
    }
    private void listener(){
        btnCapture.setOnClickListener(v -> {
            if (!currentScannedText.isEmpty()) {
                btnCapture.setEnabled(false);
                String targetWord = currentScannedText.trim().split("\\s+")[0];

                // Gọi ViewModel xử lý logic
                scannerViewModel.processScannedWord(targetWord);
            } else {
                Toast.makeText(this, "Chưa nhận diện được từ nào!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initViews() {
        viewFinder = findViewById(R.id.viewFinder);
        tvScannedText = findViewById(R.id.tvScannedText);
        btnCapture = findViewById(R.id.btnCapture);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupViewModel() {
        scannerViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);

        // 1. Lắng nghe trạng thái Loading
        scannerViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnCapture.setEnabled(!isLoading);
        });

        // 2. Lắng nghe khi xử lý xong (Lấy được dữ liệu)
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

        // 3. Lắng nghe lỗi (nếu có)
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

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
                    if (imageProxy.getImage() != null) {
                        InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());
                        textRecognizer.process(image)
                                .addOnSuccessListener(visionText -> {
                                    String text = visionText.getText();
                                    if (!text.isEmpty()) {
                                        currentScannedText = text;
                                        tvScannedText.setText(text);
                                    }
                                })
                                .addOnCompleteListener(task -> imageProxy.close());
                    }
                });

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraX", "Lỗi khởi tạo Camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            Toast.makeText(this, "Cần quyền Camera để quét chữ!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textRecognizer != null) textRecognizer.close();
    }
}