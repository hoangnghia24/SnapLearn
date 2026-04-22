package hcmute.edu.vn.snaplearn.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

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
    public interface OnUserFetchListener {
        void onSuccess(User user);
        void onError(String error);
    }
    public void getUser(String userId, OnUserFetchListener listener) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        listener.onSuccess(user);
                    } else {
                        listener.onError("Không tìm thấy dữ liệu người dùng!");
                    }
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }
    public void addUser(User user, OnUserSaveListener listener){
        db.collection("users").document(user.getUserId()).set(user)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }
    public void updateUser(User user, OnUserSaveListener listener) {
        if (user.getUserId() != null) {
            // Đóng gói duy nhất trường fullName cần cập nhật
            Map<String, Object> updates = new HashMap<>();
            updates.put("fullName", user.getFullName());

            // Dùng set() với SetOptions.merge() thay cho update()
            db.collection("users").document(user.getUserId())
                    .set(updates, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> listener.onSuccess())
                    .addOnFailureListener(e -> listener.onError(e.getMessage()));
        } else {
            listener.onError("Không tìm thấy ID người dùng!");
        }
    }
}
