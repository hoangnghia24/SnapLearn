package hcmute.edu.vn.snaplearn.models;

import com.google.firebase.firestore.DocumentId;

import java.util.List;

import hcmute.edu.vn.snaplearn.enums.ERole;

public class User {
    @DocumentId
    private String userId;
    private String fullName;
    private String gmail;
    private int studiedDay;
    private List<Flashcard> flashcards;
    private List<Exam> exams;
    private ERole role;

    public User() {
    }


    public User(String fullName, String gmail) {
        this.fullName = fullName;
        this.gmail = gmail;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }


    public int getStudiedDay() {
        return studiedDay;
    }

    public void setStudiedDay(int studiedDay) {
        this.studiedDay = studiedDay;
    }

    public List<Flashcard> getFlashCasts() {
        return flashcards;
    }

    public void setFlashCasts(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }
}
