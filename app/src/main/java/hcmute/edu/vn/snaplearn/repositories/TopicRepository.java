package hcmute.edu.vn.snaplearn.repositories;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.snaplearn.models.QuizQuestion;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.utils.QuizEngine;

public class TopicRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // READ: Lấy danh sách Topic từ Firestore
    public MutableLiveData<List<Topic>> getTopicsFromFirestore() {
        MutableLiveData<List<Topic>> topicsLiveData = new MutableLiveData<>();

        // Sử dụng addSnapshotListener để cập nhật dữ liệu thời gian thực
        db.collection("topics").addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }

            if (value != null) {
                List<Topic> topicList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    Topic topic = doc.toObject(Topic.class);
                    topic.setTopicId(doc.getId()); // Lấy ID của Document gán vào topicId
                    topicList.add(topic);
                }
                topicsLiveData.setValue(topicList);
            }
        });

        return topicsLiveData;
    }

    // CREATE: Thêm Topic mới lên Firestore
    public void addTopic(Topic topic) {
        db.collection("topics").add(topic);
    }

    // UPDATE: Cập nhật Topic đã tồn tại
    public void updateTopic(Topic topic) {
        if (topic.getTopicId() != null) {
            db.collection("topics").document(topic.getTopicId()).set(topic);
        }
    }

    // DELETE: Xóa Topic
    public void deleteTopic(String topicId) {
        db.collection("topics").document(topicId).delete();
    }
    public MutableLiveData<List<QuizQuestion>> getQuizQuestions(String topicId) {
        MutableLiveData<List<QuizQuestion>> quizLiveData = new MutableLiveData<>();

        db.collection("topics").document(topicId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                Topic topic = doc.toObject(Topic.class);
                // Kiểm tra xem topic có dữ liệu và có danh sách thẻ không
                if (topic != null && topic.getFlashcardList() != null) {
                    // Gọi QuizEngine tạo câu hỏi từ danh sách thẻ
                    List<QuizQuestion> questions = QuizEngine.generateQuiz(topic.getFlashcardList());
                    quizLiveData.setValue(questions);
                } else {
                    quizLiveData.setValue(new ArrayList<>()); // Trả về mảng rỗng nếu không có thẻ
                }
            } else {
                quizLiveData.setValue(new ArrayList<>());
            }
        }).addOnFailureListener(e -> {
            quizLiveData.setValue(null); // Báo lỗi
        });

        return quizLiveData;
    }
}