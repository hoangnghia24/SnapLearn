package hcmute.edu.vn.snaplearn.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.repositories.TopicRepository;

public class HomeViewModel extends ViewModel {
    private final TopicRepository topicRepository;
    private LiveData<List<Topic>> topics;

    public HomeViewModel() {
        topicRepository = new TopicRepository();
        // Khởi tạo LiveData từ Repository
        topics = topicRepository.getTopicsFromFirestore();
    }

    public LiveData<List<Topic>> getTopics() {
        return topics;
    }

    // Các hàm CRUD gọi từ Repository tương tự...
}
