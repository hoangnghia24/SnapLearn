package hcmute.edu.vn.snaplearn.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hcmute.edu.vn.snaplearn.models.QuizQuestion;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.repositories.TopicRepository;
import hcmute.edu.vn.snaplearn.utils.QuizEngine;

public class TopicViewModel extends ViewModel {
    private final TopicRepository repository;
    private MutableLiveData<List<QuizQuestion>> quizData = new MutableLiveData<>();

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
    // Trong TopicViewModel.java

    public void prepareQuiz(Topic topic) {
        List<QuizQuestion> questions = QuizEngine.generateQuiz(topic);
        quizData.setValue(questions);
    }

    public LiveData<List<QuizQuestion>> getQuizData() {
        return quizData;
    }
}