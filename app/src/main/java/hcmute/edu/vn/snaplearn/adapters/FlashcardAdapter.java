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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard flashcard = flashcardList.get(position);

        // Mặc định hiển thị mặt trước (Word)
        holder.tvCardWord.setText(flashcard.getWordEn());
        holder.tvCardTranscription.setText(flashcard.getTranscription());
        // Xử lý sự kiện lật thẻ đơn giản bằng cách đổi text
        holder.itemView.setOnClickListener(v -> {
            if (holder.tvCardWord.getText().toString().equals(flashcard.getWordEn())) {
                holder.tvCardWord.setText(flashcard.getWordVi());
                holder.tvCardTranscription.setText("");
                holder.tvCardWord.setTextColor(0xFF1CB0F6); // Đổi màu sang xanh khi hiện nghĩa
            } else {
                holder.tvCardWord.setText(flashcard.getWordEn());
                holder.tvCardTranscription.setText(flashcard.getTranscription());
                holder.tvCardWord.setTextColor(0xFF4B4B4B); // Về màu xám đậm ban đầu
            }
        });
    }

    @Override
    public int getItemCount() {
        return flashcardList != null ? flashcardList.size() : 0;
    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardWord;
        TextView tvCardTranscription;

        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardWord = itemView.findViewById(R.id.tvCardWord);
            tvCardTranscription = itemView.findViewById(R.id.tvCardTranscription);
        }
    }
}