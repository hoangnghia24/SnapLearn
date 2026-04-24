package hcmute.edu.vn.snaplearn.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private FirebaseAuth auth;
    public AuthRepository(){
        auth = FirebaseAuth.getInstance();
    }
    public interface OnAuthListener {
        void onSuccess(FirebaseUser firebaseUser);
        void onError(String error);
    }
    // 1. Thêm một Listener mới dành cho các hành động không trả về FirebaseUser
    public interface OnActionListener {
        void onSuccess();
        void onError(String error);
    }
    public void registerWithEmail(String email, String password, OnAuthListener listener) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification().addOnCompleteListener(verificationTask -> {
                                if (verificationTask.isSuccessful()) {
                                    listener.onSuccess(user);
                                } else {
                                    listener.onError(verificationTask.getException().getMessage());
                                }
                            });
                        }
                    } else {
                        listener.onError(task.getException().getMessage());
                    }
                });
    }
    public void loginWithEmail(String email, String password, OnAuthListener listener) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            listener.onSuccess(user);
                        }else{
                            listener.onError("Email chưa được xác thực");
                        }
                    } else {
                        listener.onError(task.getException().getMessage());
                    }
                });
    }
    // 2. Thêm hàm gửi email khôi phục mật khẩu
    public void sendPasswordResetEmail(String email, OnActionListener listener) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        if (task.getException() != null) {
                            listener.onError(task.getException().getMessage());
                        } else {
                            listener.onError("Lỗi không xác định");
                        }
                    }
                });
    }
    // Thêm vào bên trong class AuthRepository
    public void logout() {
        auth.signOut();
    }
}
