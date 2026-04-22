package hcmute.edu.vn.snaplearn.repositories;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import hcmute.edu.vn.snaplearn.models.Topic;

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
}