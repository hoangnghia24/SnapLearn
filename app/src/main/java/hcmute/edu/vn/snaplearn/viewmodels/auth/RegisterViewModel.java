package hcmute.edu.vn.snaplearn.viewmodels.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import hcmute.edu.vn.snaplearn.enums.ERole;
import hcmute.edu.vn.snaplearn.models.User;
import hcmute.edu.vn.snaplearn.repositories.AuthRepository;
import hcmute.edu.vn.snaplearn.repositories.UserRepository;

public class RegisterViewModel extends ViewModel {
    private AuthRepository authRepository;
    private UserRepository userRepository;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRegisterSuccess = new MutableLiveData<>(false);
    RegisterViewModel(){
        authRepository = new AuthRepository();
        userRepository = new UserRepository();
    }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getToastMessage() { return toastMessage; }
    public LiveData<Boolean> getIsRegisterSuccess() { return isRegisterSuccess; }
    public void register(String email, String fullName, String password, String confirmPassword) {

        if (email.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        if (password.length() < 6) {
            toastMessage.setValue("Mật khẩu phải chứa ít nhất 6 ký tự.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            toastMessage.setValue("Mật khẩu xác nhận không khớp. Vui lòng kiểm tra lại!");
            return;
        }

        isLoading.setValue(true);
        authRepository.registerWithEmail(email, password, new AuthRepository.OnAuthListener() {
            @Override
            public void onSuccess(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    User newUser = new User(fullName,email, ERole.USER);
                    newUser.setUserId(firebaseUser.getUid());
                    userRepository.addUser(newUser, new UserRepository.OnUserSaveListener() {
                        @Override
                        public void onSuccess() {
                            isLoading.setValue(false);
                            toastMessage.setValue("Tạo tài khoản thành công!");
                            isRegisterSuccess.setValue(true);
                        }

                        @Override
                        public void onError(String error) {
                            isLoading.setValue(false);
                            toastMessage.setValue("Lỗi lưu hồ sơ: " + error);
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                toastMessage.setValue("Đăng ký thất bại: " + error);
            }
        });
    }
}
