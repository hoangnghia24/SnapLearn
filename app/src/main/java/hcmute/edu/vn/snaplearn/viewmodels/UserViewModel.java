package hcmute.edu.vn.snaplearn.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hcmute.edu.vn.snaplearn.models.User;
import hcmute.edu.vn.snaplearn.repositories.UserRepository;

public class UserViewModel extends ViewModel {
    private final UserRepository repository;

    // Dùng LiveData để thông báo kết quả cập nhật về cho UI
    private final MutableLiveData<String> updateStatus = new MutableLiveData<>();

    public UserViewModel() {
        repository = new UserRepository();
    }

    public LiveData<String> getUpdateStatus() {
        return updateStatus;
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