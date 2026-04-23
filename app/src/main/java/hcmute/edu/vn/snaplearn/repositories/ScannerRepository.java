package hcmute.edu.vn.snaplearn.repositories;

import android.util.Log;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScannerRepository {

    private final Translator englishVietnameseTranslator;
    private final ExecutorService networkExecutor;

    public interface ScanProcessCallback {
        void onSuccess(String wordEn, String wordVi, String phonetic);
        void onError(String errorMsg);
    }

    public ScannerRepository() {
        networkExecutor = Executors.newSingleThreadExecutor();
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.VIETNAMESE)
                .build();
        englishVietnameseTranslator = Translation.getClient(options);
    }

    public void processWordData(String wordEn, ScanProcessCallback callback) {
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

            // Gọi dịch thuật sau khi có (hoặc không có) phiên âm
            translateWord(wordEn, phonetic, callback);
        });
    }

    private void translateWord(String wordEn, String phonetic, ScanProcessCallback callback) {
        DownloadConditions conditions = new DownloadConditions.Builder().build();
        englishVietnameseTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(v -> englishVietnameseTranslator.translate(wordEn)
                        .addOnSuccessListener(wordVi -> callback.onSuccess(wordEn, wordVi, phonetic))
                        .addOnFailureListener(e -> callback.onError("Lỗi dịch ML Kit")))
                .addOnFailureListener(e -> callback.onError("Cần mạng tải model dịch"));
    }

    public void cleanUp() {
        if (englishVietnameseTranslator != null) englishVietnameseTranslator.close();
        networkExecutor.shutdown();
    }
}