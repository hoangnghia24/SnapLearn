package hcmute.edu.vn.snaplearn.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hcmute.edu.vn.snaplearn.models.User;
import hcmute.edu.vn.snaplearn.repositories.AuthRepository;
import hcmute.edu.vn.snaplearn.repositories.UserRepository;

public class UserViewModel extends ViewModel {
    private final UserRepository repository;
    private AuthRepository authRepository;

    // Dùng LiveData để thông báo kết quả cập nhật về cho UI
    private final MutableLiveData<String> updateStatus = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoggedOut = new MutableLiveData<>(false);
    public UserViewModel() {
        repository = new UserRepository();
        authRepository = new AuthRepository();
    }

    public LiveData<String> getUpdateStatus() {
        return updateStatus;
    }
    public LiveData<Boolean> getIsLoggedOut() {
        return isLoggedOut;
    }

    public void logout() {
        authRepository.logout(); // Gọi hàm signOut() từ Repository
        isLoggedOut.setValue(true); // Cập nhật trạng thái để View biết
    }
    public void updateUserInfo(User user) {
        repository.updateUser(user, new UserRepository.OnUserSaveListener() {
            @Override
            public void onSuccess() {
                updateStatus.setValue("SUCCESS");
            }

            @Override
            public void onError(String error) {
                updateStatus.setValue("Lỗi: " + error);
            }
        });
    }
}