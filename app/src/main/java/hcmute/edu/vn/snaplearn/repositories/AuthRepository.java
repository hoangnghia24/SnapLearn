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
}
