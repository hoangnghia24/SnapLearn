package hcmute.edu.vn.snaplearn.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.adapters.QuizAdapter;
import hcmute.edu.vn.snaplearn.models.Flashcard;
import hcmute.edu.vn.snaplearn.models.QuizQuestion;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.repositories.TopicRepository;
import hcmute.edu.vn.snaplearn.utils.QuizEngine;
import hcmute.edu.vn.snaplearn.viewmodels.TopicViewModel;

public class QuestionFragment extends Fragment {

    private ProgressBar pbQuiz;
    private TextView tvQuizProgress;
    private ViewPager2 vpQuiz; // Sử dụng ViewPager2 để chứa danh sách câu hỏi
    private AppCompatButton btnCheck;
    private ImageButton btnClose;

    private List<QuizQuestion> quizQuestions;
    private QuizAdapter quizAdapter;
    private LinearLayout layoutFeedback;
    private TextView tvFeedbackMessage;
    private AppCompatButton btnNext;
    private String currentSelectedAnswer = "";
    private AppCompatButton currentSelectedButton = null;
    private boolean isAnswerChecked = false;

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
        setupViewPager();
        setupListeners();
    }

    private void initViews(View view) {
        pbQuiz = view.findViewById(R.id.pbQuiz);
        tvQuizProgress = view.findViewById(R.id.tvQuizProgress);
        vpQuiz = view.findViewById(R.id.vpQuiz);
        btnCheck = view.findViewById(R.id.btnCheck);
        btnClose = view.findViewById(R.id.btnClose);

        // Ánh xạ UI mới
        layoutFeedback = view.findViewById(R.id.layoutFeedback);
        tvFeedbackMessage = view.findViewById(R.id.tvFeedbackMessage);
        btnNext = view.findViewById(R.id.btnNext);

        vpQuiz.setUserInputEnabled(false);
    }

    // Trong file QuestionFragment.java
    // Sửa lại hàm setupData() trong QuestionFragment.java
    private void setupData() {
        if (getArguments() != null) {
            String topicId = getArguments().getString("TOPIC_ID");

            if (topicId != null) {
                // CHUẨN MVVM: Gọi ViewModel để lấy dữ liệu
                TopicViewModel topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);

                // Lắng nghe dữ liệu câu hỏi từ ViewModel
                topicViewModel.getQuizQuestions(topicId).observe(getViewLifecycleOwner(), questions -> {
                    if (questions != null && !questions.isEmpty()) {
                        // Dữ liệu đã sẵn sàng, gán vào và vẽ giao diện
                        quizQuestions = questions;
                        pbQuiz.setMax(quizQuestions.size());
                        setupViewPager();
                    } else {
                        Toast.makeText(getContext(), "Chủ đề này không đủ từ vựng (cần ít nhất 4 từ)!", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
            }
        }
    }

    private void setupViewPager() {
        if (quizQuestions == null || quizQuestions.isEmpty()) return;

        // Gọi Adapter và lắng nghe sự kiện chọn đáp án từ mỗi item
        quizAdapter = new QuizAdapter(quizQuestions, (answer, selectedButton) -> {
            if (!isAnswerChecked) {
                currentSelectedAnswer = answer;
                currentSelectedButton = selectedButton;
            }
        });

        vpQuiz.setAdapter(quizAdapter);
        updateProgressUI(0);
    }

    private void setupListeners() {
        // Nút Kiểm tra giờ chỉ làm đúng nhiệm vụ kiểm tra
        btnCheck.setOnClickListener(v -> checkCurrentAnswer());

        // Nút Next trên khung thông báo mới làm nhiệm vụ chuyển câu
        btnNext.setOnClickListener(v -> moveToNextQuestion());

        btnClose.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());
    }

    private void checkCurrentAnswer() {
        if (currentSelectedAnswer.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn 1 đáp án!", Toast.LENGTH_SHORT).show();
            return;
        }

        QuizQuestion currentQuestion = quizQuestions.get(vpQuiz.getCurrentItem());

        // Ẩn nút Kiểm tra, hiện khung Feedback
        btnCheck.setVisibility(View.GONE);
        layoutFeedback.setVisibility(View.VISIBLE);

        if (currentSelectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            // Giao diện đúng (Mô phỏng SVG màu xanh)
            currentSelectedButton.setBackgroundResource(R.drawable.bg_quiz_option_correct);

            layoutFeedback.setBackgroundColor(android.graphics.Color.parseColor("#D7FFB8"));
            tvFeedbackMessage.setText("Chính xác tuyệt đối!");
            tvFeedbackMessage.setTextColor(android.graphics.Color.parseColor("#58CC02"));
            btnNext.setBackgroundColor(android.graphics.Color.parseColor("#58CC02"));

        } else {
            // Giao diện sai (Mô phỏng SVG màu đỏ)
            currentSelectedButton.setBackgroundResource(R.drawable.bg_quiz_option_wrong);

            layoutFeedback.setBackgroundColor(android.graphics.Color.parseColor("#FB5B5D"));
            tvFeedbackMessage.setText("Sai rồi! Đáp án: " + currentQuestion.getCorrectAnswer());
            tvFeedbackMessage.setTextColor(android.graphics.Color.WHITE);
            btnNext.setBackgroundColor(android.graphics.Color.parseColor("#A30205"));
        }

        isAnswerChecked = true;
    }
    private void moveToNextQuestion() {
        int nextItem = vpQuiz.getCurrentItem() + 1;

        if (nextItem < quizQuestions.size()) {
            isAnswerChecked = false;
            currentSelectedAnswer = "";

            // Trả UI về trạng thái ban đầu để làm câu tiếp theo
            layoutFeedback.setVisibility(View.GONE);
            btnCheck.setVisibility(View.VISIBLE);

            vpQuiz.setCurrentItem(nextItem, true);
            updateProgressUI(nextItem);
        } else {
            Toast.makeText(getContext(), "Chúc mừng! Bạn đã hoàn thành bài tập.", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void updateProgressUI(int index) {
        tvQuizProgress.setText((index + 1) + "/" + quizQuestions.size());
        pbQuiz.setProgress(index + 1);
    }
}