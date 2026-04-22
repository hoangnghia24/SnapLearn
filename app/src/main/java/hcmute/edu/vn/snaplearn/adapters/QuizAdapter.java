package hcmute.edu.vn.snaplearn.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.models.QuizQuestion;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private List<QuizQuestion> quizQuestions;
    private OnAnswerSelectedListener listener;

    // Interface để gửi sự kiện chọn đáp án về Fragment/Activity xử lý logic "Kiểm tra"
    public interface OnAnswerSelectedListener {
        void onAnswerSelected(String answer, AppCompatButton selectedButton);
    }

    public QuizAdapter(List<QuizQuestion> quizQuestions, OnAnswerSelectedListener listener) {
        this.quizQuestions = quizQuestions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_question, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        holder.bind(quizQuestions.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return quizQuestions != null ? quizQuestions.size() : 0;
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionWord;
        AppCompatButton btnOption1, btnOption2, btnOption3, btnOption4;
        AppCompatButton lastSelectedButton = null;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionWord = itemView.findViewById(R.id.tvQuestionWord);
            btnOption1 = itemView.findViewById(R.id.btnOption1);
            btnOption2 = itemView.findViewById(R.id.btnOption2);
            btnOption3 = itemView.findViewById(R.id.btnOption3);
            btnOption4 = itemView.findViewById(R.id.btnOption4);
        }

        public void bind(QuizQuestion question, OnAnswerSelectedListener listener) {
            tvQuestionWord.setText(question.getQuestionContent());

            List<String> options = question.getOptions();
            setupOption(btnOption1, "1   " + options.get(0), listener);
            setupOption(btnOption2, "2   " + options.get(1), listener);
            setupOption(btnOption3, "3   " + options.get(2), listener);
            setupOption(btnOption4, "4   " + options.get(3), listener);
        }

        private void setupOption(AppCompatButton button, String text, OnAnswerSelectedListener listener) {
            button.setText(text);
            button.setOnClickListener(v -> {
                resetOptionsUI();
                button.setBackgroundResource(R.drawable.bg_quiz_option_selected);
                button.setTextColor(Color.parseColor("#1CB0F6"));
                lastSelectedButton = button;

                // Gửi đáp án về cho Fragment xử lý logic "Kiểm tra"
                String answerValue = text.substring(4);
                listener.onAnswerSelected(answerValue, button);
            });
        }

        private void resetOptionsUI() {
            AppCompatButton[] buttons = {btnOption1, btnOption2, btnOption3, btnOption4};
            for (AppCompatButton b : buttons) {
                b.setBackgroundResource(R.drawable.bg_quiz_option_normal);
                b.setTextColor(Color.parseColor("#4B4B4B"));
            }
        }
    }
}