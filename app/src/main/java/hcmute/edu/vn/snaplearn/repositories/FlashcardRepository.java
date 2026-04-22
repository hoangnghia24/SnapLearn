package hcmute.edu.vn.snaplearn.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import hcmute.edu.vn.snaplearn.models.Flashcard;

public class FlashcardRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * CREATE: Thêm 1 Flashcard mới vào một Topic đã có.
     * Sử dụng FieldValue.arrayUnion để Firebase tự động "nhét" thẻ này vào mảng
     * mà không cần phải tải toàn bộ danh sách cũ về máy (Tiết kiệm băng thông và chi phí).
     */
    public Task<Void> addFlashcardToTopic(String topicId, Flashcard newFlashcard) {
        DocumentReference topicRef = db.collection("topics").document(topicId);
        return topicRef.update("flashcardList", FieldValue.arrayUnion(newFlashcard));
    }

    /**
     * DELETE: Xóa 1 Flashcard khỏi Topic.
     * FieldValue.arrayRemove sẽ tìm và xóa phần tử khớp chính xác với object bạn truyền vào.
     */
    public Task<Void> removeFlashcardFromTopic(String topicId, Flashcard flashcardToRemove) {
        DocumentReference topicRef = db.collection("topics").document(topicId);
        return topicRef.update("flashcardList", FieldValue.arrayRemove(flashcardToRemove));
    }

    /**
     * LƯU Ý VỀ TÍNH NĂNG UPDATE (SỬA THẺ):
     * Firestore KHÔNG hỗ trợ sửa trực tiếp 1 phần tử nằm giữa mảng (ví dụ: sửa phần tử thứ 3).
     * Để cập nhật toàn bộ danh sách thẻ (như khi bạn nhấn nút "Lưu" ở TopicFragment),
     * bạn nên sử dụng hàm cập nhật toàn bộ mảng.
     */
    public Task<Void> updateAllFlashcardsInTopic(String topicId, java.util.List<Flashcard> newList) {
        DocumentReference topicRef = db.collection("topics").document(topicId);
        return topicRef.update("flashcardList", newList);
    }
}