package hcmute.edu.vn.snaplearn.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hcmute.edu.vn.snaplearn.models.Flashcard;
import hcmute.edu.vn.snaplearn.models.QuizQuestion;

public class QuizEngine {

    public static List<QuizQuestion> generateQuiz(List<Flashcard> flashcardList) {
        List<QuizQuestion> questions = new ArrayList<>();

        // Kiểm tra an toàn: Cần ít nhất 4 flashcard để tạo 4 đáp án (1 đúng, 3 sai)
        if (flashcardList == null || flashcardList.size() < 4) {
            return questions;
        }

        for (Flashcard currentCard : flashcardList) {
            List<String> options = new ArrayList<>();
            options.add(currentCard.getWordVi()); // Thêm đáp án đúng

            // Tạo danh sách trộn để lấy ngẫu nhiên các đáp án sai
            List<Flashcard> shuffleList = new ArrayList<>(flashcardList);
            shuffleList.remove(currentCard); // Loại bỏ đáp án đúng ra khỏi danh sách nhiễu
            Collections.shuffle(shuffleList);

            // Lấy 3 đáp án sai (từ tiếng Việt của các flashcard khác)
            for (int i = 0; i < 3; i++) {
                options.add(shuffleList.get(i).getWordVi());
            }

            // Trộn thứ tự 4 đáp án lên để vị trí đáp án đúng thay đổi ngẫu nhiên
            Collections.shuffle(options);

            // Tạo câu hỏi mới theo đúng thứ tự constructor của QuizQuestion.java
            questions.add(new QuizQuestion(
                    currentCard.getWordEn(),   // questionContent
                    currentCard.getWordVi(),   // correctAnswer
                    options                    // 4 lựa chọn
            ));
        }

        // Trộn thứ tự các câu hỏi trong bài kiểm tra
        Collections.shuffle(questions);
        return questions;
    }
}