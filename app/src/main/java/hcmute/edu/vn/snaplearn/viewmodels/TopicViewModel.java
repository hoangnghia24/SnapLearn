package hcmute.edu.vn.snaplearn.viewmodels;

import androidx.lifecycle.ViewModel;

import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.repositories.TopicRepository;

public class TopicViewModel extends ViewModel {
    private final TopicRepository repository;

    public TopicViewModel() {
        repository = new TopicRepository();
    }

    // Gọi Repository để tạo mới
    public void addTopic(Topic topic) {
        repository.addTopic(topic);
    }

    // Gọi Repository để cập nhật
    public void updateTopic(Topic topic) {
        repository.updateTopic(topic);
    }
    public void deleteTopic(String topicId) {
        repository.deleteTopic(topicId);
    }
}