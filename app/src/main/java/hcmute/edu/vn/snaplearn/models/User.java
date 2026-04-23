package hcmute.edu.vn.snaplearn.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.List;

import hcmute.edu.vn.snaplearn.enums.ERole;

public class User implements Serializable {
    @DocumentId
    private String userId;
    private String fullName;
    private String gmail;
    private ERole role;

    public User() {
    }


    public User(String fullName, String gmail) {
        this.fullName = fullName;
        this.gmail = gmail;
    }

    public User(String fullName, String email, ERole eRole) {
        this.fullName = fullName;
        this.gmail = email;
        this.role = eRole;
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

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }
}
