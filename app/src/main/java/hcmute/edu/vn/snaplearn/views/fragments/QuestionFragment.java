package hcmute.edu.vn.snaplearn.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.models.QuizQuestion;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.utils.QuizEngine;

public class QuestionFragment extends Fragment {

    private ProgressBar pbQuiz;
    private TextView tvQuizProgress, tvQuestionWord;
    private AppCompatButton btnOption1, btnOption2, btnOption3, btnOption4, btnCheck;
    private ImageButton btnClose;

    private List<QuizQuestion> quizQuestions;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private AppCompatButton lastSelectedButton = null;
    private boolean isAnswerChecked = false; // Trạng thái để đổi nút "Kiểm tra" thành "Tiếp theo"

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupData();
        displayQuestion();
        setupListeners();
    }

    private void initViews(View view) {
        pbQuiz = view.findViewById(R.id.pbQuiz);
        tvQuizProgress = view.findViewById(R.id.tvQuizProgress);
        tvQuestionWord = view.findViewById(R.id.tvQuestionWord);
        btnCheck = view.findViewById(R.id.btnCheck);
        btnClose = view.findViewById(R.id.btnClose);

        // Giả sử trong layoutOptions bạn có 4 nút với ID tương ứng
        // Bạn có thể tìm theo ID cụ thể nếu bạn đã đặt trong XML
        ViewGroup layoutOptions = view.findViewById(R.id.layoutOptions);
        btnOption1 = (AppCompatButton) layoutOptions.getChildAt(0);
        btnOption2 = (AppCompatButton) layoutOptions.getChildAt(1);
        btnOption3 = (AppCompatButton) layoutOptions.getChildAt(2);
        btnOption4 = (AppCompatButton) layoutOptions.getChildAt(3);
    }

    private void setupData() {
        if (getArguments() != null) {
            Topic topic = (Topic) getArguments().getSerializable("QUIZ_TOPIC");
            if (topic != null) {
                // Sử dụng QuizEngine đã tạo ở bước trước để sinh câu hỏi
                quizQuestions = QuizEngine.generateQuiz(topic);
                pbQuiz.setMax(quizQuestions.size());
            }
        }
    }

    private void displayQuestion() {
        if (quizQuestions == null || quizQuestions.isEmpty() || currentQuestionIndex >= quizQuestions.size()) {
            Toast.makeText(getContext(), "Hoàn thành bài tập!", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
            return;
        }

        resetOptionsUI();
        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);

        // Cập nhật giao diện câu hỏi
        tvQuestionWord.setText(currentQuestion.getQuestionContent());
        tvQuizProgress.setText((currentQuestionIndex + 1) + "/" + quizQuestions.size());
        pbQuiz.setProgress(currentQuestionIndex + 1);

        // Đổ đáp án vào các nút
        List<String> options = currentQuestion.getOptions();
        btnOption1.setText("1   " + options.get(0));
        btnOption2.setText("2   " + options.get(1));
        btnOption3.setText("3   " + options.get(2));
        btnOption4.setText("4   " + options.get(3));

        isAnswerChecked = false;
        btnCheck.setText("KIỂM TRA");
        selectedAnswer = "";
    }

    private void setupListeners() {
        View.OnClickListener optionClickListener = v -> {
            if (isAnswerChecked) return; // Nếu đã kiểm tra đáp án thì không cho chọn lại

            if (lastSelectedButton != null) {
                lastSelectedButton.setBackgroundResource(R.drawable.bg_quiz_option_normal);
                lastSelectedButton.setTextColor(Color.parseColor("#4B4B4B"));
            }

            lastSelectedButton = (AppCompatButton) v;
            lastSelectedButton.setBackgroundResource(R.drawable.bg_quiz_option_selected);
            lastSelectedButton.setTextColor(Color.parseColor("#1CB0F6"));

            // Lấy text đáp án (bỏ phần số thứ tự 1, 2, 3... ở đầu)
            selectedAnswer = lastSelectedButton.getText().toString().substring(4);
        };

        btnOption1.setOnClickListener(optionClickListener);
        btnOption2.setOnClickListener(optionClickListener);
        btnOption3.setOnClickListener(optionClickListener);
        btnOption4.setOnClickListener(optionClickListener);

        btnCheck.setOnClickListener(v -> {
            if (isAnswerChecked) {
                // Chuyển sang câu tiếp theo
                currentQuestionIndex++;
                displayQuestion();
            } else {
                handleCheckAnswer();
            }
        });

        btnClose.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());
    }

    private void handleCheckAnswer() {
        if (selectedAnswer.isEmpty()) {
            Toast.makeText(getContext(), "Hãy chọn một đáp án!", Toast.LENGTH_SHORT).show();
            return;
        }

        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            // Đúng: Đổi màu nút đã chọn sang xanh lá (Success)
            lastSelectedButton.setBackgroundResource(R.drawable.bg_quiz_progress); // Tái sử dụng màu xanh lá
            lastSelectedButton.setTextColor(Color.WHITE);
            Toast.makeText(getContext(), "Chính xác!", Toast.LENGTH_SHORT).show();
        } else {
            // Sai: Đổi màu nút đã chọn sang đỏ (tùy chọn tạo thêm drawable màu đỏ)
            lastSelectedButton.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Sai rồi! Đáp án là: " + currentQuestion.getCorrectAnswer(), Toast.LENGTH_LONG).show();
        }

        isAnswerChecked = true;
        btnCheck.setText("TIẾP THEO");
    }

    private void resetOptionsUI() {
        btnOption1.setBackgroundResource(R.drawable.bg_quiz_option_normal);
        btnOption2.setBackgroundResource(R.drawable.bg_quiz_option_normal);
        btnOption3.setBackgroundResource(R.drawable.bg_quiz_option_normal);
        btnOption4.setBackgroundResource(R.drawable.bg_quiz_option_normal);

        btnOption1.setTextColor(Color.parseColor("#4B4B4B"));
        btnOption2.setTextColor(Color.parseColor("#4B4B4B"));
        btnOption3.setTextColor(Color.parseColor("#4B4B4B"));
        btnOption4.setTextColor(Color.parseColor("#4B4B4B"));

        lastSelectedButton = null;
    }
}