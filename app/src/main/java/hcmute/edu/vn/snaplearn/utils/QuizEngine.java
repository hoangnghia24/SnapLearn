package hcmute.edu.vn.snaplearn.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import hcmute.edu.vn.snaplearn.models.Flashcard;
import hcmute.edu.vn.snaplearn.models.QuizQuestion;
import hcmute.edu.vn.snaplearn.models.Topic;

public class QuizEngine {

    public static List<QuizQuestion> generateQuiz(Topic topic) {
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        List<Flashcard> allCards = topic.getFlashcardList();

        // Điều kiện tối thiểu để tạo trắc nghiệm 4 đáp án là có ít nhất 4 thẻ
        if (allCards == null || allCards.size() < 4) return quizQuestions;

        for (Flashcard currentCard : allCards) {
            String question = currentCard.getWordEn(); // Câu hỏi là từ tiếng Anh
            String correctAns = currentCard.getWordVi(); // Đáp án đúng là nghĩa tiếng Việt

            // 1. Tạo danh sách các lựa chọn
            List<String> options = new ArrayList<>();
            options.add(correctAns);

            // 2. Lấy ngẫu nhiên 3 đáp án sai (Distractors) từ các thẻ khác
            List<Flashcard> otherCards = new ArrayList<>(allCards);
            otherCards.remove(currentCard); // Loại bỏ thẻ hiện tại khỏi danh sách đáp án sai
            Collections.shuffle(otherCards); // Xáo trộn để lấy ngẫu nhiên

            for (int i = 0; i < 3; i++) {
                options.add(otherCards.get(i).getWordVi());
            }

            // 3. Xáo trộn lại 4 đáp án để vị trí đáp án đúng không cố định
            Collections.shuffle(options);

            quizQuestions.add(new QuizQuestion(question, correctAns, options));
        }

        // Xáo trộn thứ tự các câu hỏi trong bài kiểm tra
        Collections.shuffle(quizQuestions);
        return quizQuestions;
    }
}