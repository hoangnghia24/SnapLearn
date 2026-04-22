package hcmute.edu.vn.snaplearn.models;

import java.io.Serializable;
import java.util.List;

public class QuizQuestion implements Serializable {
    private String questionContent; // Từ tiếng Anh cần hỏi
    private String correctAnswer;   // Nghĩa đúng (tiếng Việt)
    private List<String> options;    // 4 đáp án để người dùng chọn

    public QuizQuestion(String questionContent, String correctAnswer, List<String> options) {
        this.questionContent = questionContent;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    // Getters
    public String getQuestionContent() { return questionContent; }
    public String getCorrectAnswer() { return correctAnswer; }
    public List<String> getOptions() { return options; }
}