package hcmute.edu.vn.snaplearn.models;

import java.io.Serializable;

public class Flashcard implements Serializable {
    private String wordEn;
    private String wordVi;
    private String transcription;

    public Flashcard() {
    }

    public Flashcard( String wordEn, String wordVi, String transcription) {
        this.wordEn = wordEn;
        this.wordVi = wordVi;
        this.transcription = transcription;
    }


    public String getWordEn() {
        return wordEn;
    }

    public void setWordEn(String wordEn) {
        this.wordEn = wordEn;
    }

    public String getWordVi() {
        return wordVi;
    }

    public void setWordVi(String wordVi) {
        this.wordVi = wordVi;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }
}
