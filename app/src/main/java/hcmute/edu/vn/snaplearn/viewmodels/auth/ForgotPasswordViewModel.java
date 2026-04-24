package hcmute.edu.vn.snaplearn.viewmodels.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hcmute.edu.vn.snaplearn.repositories.AuthRepository;

public class ForgotPasswordViewModel extends ViewModel {

    private AuthRepository authRepository;

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSuccess = new MutableLiveData<>(false);

    // Khởi tạo Repository trực tiếp giống như bạn đã làm ở Login/Register
    public ForgotPasswordViewModel() {
        authRepository = new AuthRepository();
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getToastMessage() { return toastMessage; }
    public LiveData<Boolean> getIsSuccess() { return isSuccess; }

    public void resetPassword(String email) {
        if (email.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập email của bạn.");
            return;
        }

        isLoading.setValue(true);

        authRepository.sendPasswordResetEmail(email, new AuthRepository.OnActionListener() {
            @Override
            public void onSuccess() {
                isLoading.setValue(false);
                toastMessage.setValue("Đã gửi email khôi phục mật khẩu. Vui lòng kiểm tra hộp thư!");
                isSuccess.setValue(true);
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                toastMessage.setValue("Lỗi: " + error);
            }
        });
    }
}