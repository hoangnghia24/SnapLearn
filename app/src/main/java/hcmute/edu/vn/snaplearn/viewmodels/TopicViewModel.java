package hcmute.edu.vn.snaplearn.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hcmute.edu.vn.snaplearn.models.QuizQuestion;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.repositories.TopicRepository;

public class TopicViewModel extends ViewModel {
    private final TopicRepository repository;

    // Lưu trữ danh sách Topic để dùng cho Home/Learn Fragment
    private LiveData<List<Topic>> topicsLiveData;

    public TopicViewModel() {
        repository = new TopicRepository();
    }

    // ===============================
    // CÁC HÀM READ (ĐỌC DỮ LIỆU)
    // ===============================

    // Trả về LiveData danh sách Topic cho LearnFragment quan sát
    public LiveData<List<Topic>> getTopics() {
        if (topicsLiveData == null) {
            topicsLiveData = repository.getTopicsFromFirestore();
        }
        return topicsLiveData;
    }

    // Trả về LiveData bộ câu hỏi dựa theo ID cho QuestionFragment quan sát
    public LiveData<List<QuizQuestion>> getQuizQuestions(String topicId) {
        return repository.getQuizQuestions(topicId);
    }

    // ===============================
    // CÁC HÀM WRITE (GHI DỮ LIỆU)
    // ===============================

    public void addTopic(Topic topic) {
        repository.addTopic(topic);
    }

    public void updateTopic(Topic topic) {
        repository.updateTopic(topic);
    }

    public void deleteTopic(String topicId) {
        repository.deleteTopic(topicId);
    }
}