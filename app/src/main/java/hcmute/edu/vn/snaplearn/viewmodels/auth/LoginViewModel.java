package hcmute.edu.vn.snaplearn.viewmodels.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import hcmute.edu.vn.snaplearn.repositories.AuthRepository;
import hcmute.edu.vn.snaplearn.repositories.UserRepository;

public class LoginViewModel extends ViewModel {
    private AuthRepository authRepository;
    private UserRepository userRepository;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoginSuccess = new MutableLiveData<>(false);
    public LoginViewModel(){
        authRepository = new AuthRepository();
        userRepository = new UserRepository();
    }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getToastMessage() { return toastMessage; }
    public LiveData<Boolean> getIsLoginSuccess() { return isLoginSuccess; }
    public void login(String email, String password){
        if(email.isEmpty() || password.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập đầy đủ thông tin.");
            return;
        }
        isLoading.setValue(true);
        authRepository.loginWithEmail(email, password, new AuthRepository.OnAuthListener() {
            @Override
            public void onSuccess(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    isLoading.setValue(false);
                    toastMessage.setValue("Đăng nhập thành công!");
                    isLoginSuccess.setValue(true);
                }
            }
            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                toastMessage.setValue("Đăng nhập thất bại: " + error);
            }
        });
    }

}
