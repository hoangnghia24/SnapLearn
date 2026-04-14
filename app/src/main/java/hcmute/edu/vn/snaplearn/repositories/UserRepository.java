package hcmute.edu.vn.snaplearn.repositories;

import com.google.firebase.firestore.FirebaseFirestore;

import hcmute.edu.vn.snaplearn.models.User;

public class UserRepository {
    private FirebaseFirestore db;
    public UserRepository(){
        db = FirebaseFirestore.getInstance();
    }
    public interface OnUserSaveListener {
        void onSuccess();
        void onError(String error);
    }
    public void addUser(User user, OnUserSaveListener listener){
        db.collection("users").document(user.getUserId()).set(user)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }
}
