package hcmute.edu.vn.snaplearn.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.models.Flashcard;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {

    private List<Flashcard> flashcardList;

    public FlashcardAdapter(List<Flashcard> flashcardList) {
        this.flashcardList = flashcardList;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        holder.bind(flashcardList.get(position));
    }

    @Override
    public int getItemCount() {
        return flashcardList != null ? flashcardList.size() : 0;
    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCardWord;
        private final TextView tvCardTranscription;

        private boolean isShowingFront = true;

        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardWord = itemView.findViewById(R.id.tvCardWord);
            tvCardTranscription = itemView.findViewById(R.id.tvCardTranscription);
        }

        public void bind(Flashcard flashcard) {
            isShowingFront = true;
            showFrontSide(flashcard);

            setupFlipListener(flashcard);
        }

        private void setupFlipListener(Flashcard flashcard) {
            itemView.setOnClickListener(v -> {
                // Đảo ngược trạng thái
                isShowingFront = !isShowingFront;

                // Cập nhật UI theo trạng thái mới
                if (isShowingFront) {
                    showFrontSide(flashcard);
                } else {
                    showBackSide(flashcard);
                }
            });
        }

        private void showFrontSide(Flashcard flashcard) {
            tvCardWord.setText(flashcard.getWordEn());
            tvCardTranscription.setText(flashcard.getTranscription());

            // Màu xám đậm mặc định
            tvCardWord.setTextColor(0xFF4B4B4B);
            tvCardTranscription.setVisibility(View.VISIBLE);
        }

        private void showBackSide(Flashcard flashcard) {
            tvCardWord.setText(flashcard.getWordVi());

            // Màu xanh đặc trưng khi hiện nghĩa
            tvCardWord.setTextColor(0xFF1CB0F6);

            // Ẩn view phiên âm thay vì set text rỗng để tối ưu layout
            tvCardTranscription.setVisibility(View.GONE);
        }
    }
}