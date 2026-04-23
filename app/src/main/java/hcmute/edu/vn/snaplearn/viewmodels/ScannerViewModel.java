package hcmute.edu.vn.snaplearn.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hcmute.edu.vn.snaplearn.repositories.ScannerRepository;

public class ScannerViewModel extends ViewModel {

    private final ScannerRepository repository;

    // Các biến LiveData để Activity quan sát (observe)
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String[]> scanResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ScannerViewModel() {
        repository = new ScannerRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String[]> getScanResult() {
        return scanResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void processScannedWord(String wordEn) {
        isLoading.postValue(true); // Bật ProgressBar

        repository.processWordData(wordEn, new ScannerRepository.ScanProcessCallback() {
            @Override
            public void onSuccess(String wordEn, String wordVi, String phonetic) {
                isLoading.postValue(false);
                // Đóng gói mảng String trả về cho Activity
                scanResult.postValue(new String[]{wordEn, wordVi, phonetic});
            }

            @Override
            public void onError(String errorMsg) {
                isLoading.postValue(false);
                errorMessage.postValue(errorMsg);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.cleanUp(); // Giải phóng bộ nhớ khi đóng màn hình
    }
}