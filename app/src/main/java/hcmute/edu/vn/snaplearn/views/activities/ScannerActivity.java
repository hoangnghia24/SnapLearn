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

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hcmute.edu.vn.snaplearn.R;

@ExperimentalGetImage
public class ScannerActivity extends AppCompatActivity {

    private PreviewView viewFinder;
    private TextView tvScannedText;
    private Button btnCapture;
    private ProgressBar progressBar;

    private String currentScannedText = "";
    private TextRecognizer textRecognizer;
    private Translator englishVietnameseTranslator;
    private ExecutorService networkExecutor;

    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        viewFinder = findViewById(R.id.viewFinder);
        tvScannedText = findViewById(R.id.tvScannedText);
        btnCapture = findViewById(R.id.btnCapture);
        progressBar = findViewById(R.id.progressBar);

        networkExecutor = Executors.newSingleThreadExecutor();
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // Khởi tạo ML Kit Translate (Anh -> Việt)
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.VIETNAMESE)
                .build();
        englishVietnameseTranslator = Translation.getClient(options);

        // Xin quyền Camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        btnCapture.setOnClickListener(v -> {
            if (!currentScannedText.isEmpty()) {
                // Tạm dừng tương tác
                btnCapture.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                // Lấy từ đầu tiên nếu máy ảnh quét dính cả câu
                String targetWord = currentScannedText.trim().split("\\s+")[0];

                processWord(targetWord);
            } else {
                Toast.makeText(this, "Chưa nhận diện được từ nào!", Toast.LENGTH_SHORT).show();
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
                                        tvScannedText.setText(text); // Hiển thị realtime lên màn hình
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

    // 1. Gọi API lấy Phiên Âm
    private void processWord(String wordEn) {
        networkExecutor.execute(() -> {
            String phonetic = "";
            try {
                URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + wordEn);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) response.append(line);
                    in.close();

                    JSONArray jsonArray = new JSONArray(response.toString());
                    JSONObject wordObj = jsonArray.getJSONObject(0);

                    // API này lưu phiên âm ở nhiều nơi khác nhau, cần check kỹ
                    if (wordObj.has("phonetic")) {
                        phonetic = wordObj.getString("phonetic");
                    } else if (wordObj.has("phonetics")) {
                        JSONArray phoneticsArray = wordObj.getJSONArray("phonetics");
                        for (int i = 0; i < phoneticsArray.length(); i++) {
                            JSONObject p = phoneticsArray.getJSONObject(i);
                            if (p.has("text") && !p.getString("text").isEmpty()) {
                                phonetic = p.getString("text");
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("API_ERROR", "Không tìm thấy phiên âm", e);
            }

            // Gọi bước 2: Dịch nghĩa trên Main Thread
            String finalPhonetic = phonetic;
            runOnUiThread(() -> translateToVietnamese(wordEn, finalPhonetic));
        });
    }

    // 2. Gọi ML Kit Dịch nghĩa
    private void translateToVietnamese(String wordEn, String phonetic) {
        DownloadConditions conditions = new DownloadConditions.Builder().build();
        englishVietnameseTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(v -> {
                    englishVietnameseTranslator.translate(wordEn)
                            .addOnSuccessListener(wordVi -> finishWithResult(wordEn, wordVi, phonetic))
                            .addOnFailureListener(e -> finishWithResult(wordEn, "Lỗi dịch", phonetic));
                })
                .addOnFailureListener(e -> finishWithResult(wordEn, "Cần mạng tải model dịch", phonetic));
    }

    // 3. Trả dữ liệu về TopicFragment
    private void finishWithResult(String wordEn, String wordVi, String phonetic) {
        Intent intent = new Intent();
        intent.putExtra("SCANNED_WORD_EN", wordEn);
        intent.putExtra("TRANSLATED_WORD_VI", wordVi);
        intent.putExtra("PHONETIC_TEXT", phonetic);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Cần quyền Camera để quét chữ!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (englishVietnameseTranslator != null) englishVietnameseTranslator.close();
        if (textRecognizer != null) textRecognizer.close();
        networkExecutor.shutdown();
    }
}