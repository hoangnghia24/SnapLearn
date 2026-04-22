package hcmute.edu.vn.snaplearn.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.models.Flashcard;

public class FlashcardEditAdapter extends RecyclerView.Adapter<FlashcardEditAdapter.EditViewHolder> {

    private List<Flashcard> listEditCards;

    public FlashcardEditAdapter(List<Flashcard> listEditCards) {
        this.listEditCards = listEditCards;
    }

    @NonNull
    @Override
    public EditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_flashcard, parent, false);
        return new EditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditViewHolder holder, int position) {
        // Adapter giờ đây rất "sạch", chỉ gọi hàm bind của ViewHolder
        holder.bind(listEditCards.get(position));
    }

    @Override
    public int getItemCount() {
        return listEditCards != null ? listEditCards.size() : 0;
    }

    public static class EditViewHolder extends RecyclerView.ViewHolder {
        private final EditText edtWord, edtTranscription, edtMeaning;
        private TextWatcher wordWatcher, transWatcher, meanWatcher;

        public EditViewHolder(@NonNull View itemView) {
            super(itemView);
            edtWord = itemView.findViewById(R.id.edtWord);
            edtTranscription = itemView.findViewById(R.id.edtTranscription);
            edtMeaning = itemView.findViewById(R.id.edtMeaning);
        }

        // Đưa toàn bộ logic xử lý UI và Event vào đây (Đảm bảo tính Đóng gói - Encapsulation)
        public void bind(Flashcard card) {
            removeOldWatchers();

            // Đổ dữ liệu
            edtWord.setText(card.getWordEn());
            edtTranscription.setText(card.getTranscription());
            edtMeaning.setText(card.getWordVi());

            setupNewWatchers(card);
        }

        private void removeOldWatchers() {
            if (wordWatcher != null) edtWord.removeTextChangedListener(wordWatcher);
            if (transWatcher != null) edtTranscription.removeTextChangedListener(transWatcher);
            if (meanWatcher != null) edtMeaning.removeTextChangedListener(meanWatcher);
        }

        private void setupNewWatchers(Flashcard card) {
            wordWatcher = new SimpleTextWatcher(s -> card.setWordEn(s));
            transWatcher = new SimpleTextWatcher(s -> card.setTranscription(s));
            meanWatcher = new SimpleTextWatcher(s -> card.setWordVi(s));

            edtWord.addTextChangedListener(wordWatcher);
            edtTranscription.addTextChangedListener(transWatcher);
            edtMeaning.addTextChangedListener(meanWatcher);
        }
    }

    interface OnTextChange { void onUpdate(String s); }

    static class SimpleTextWatcher implements TextWatcher {
        private final OnTextChange listener;
        SimpleTextWatcher(OnTextChange listener) { this.listener = listener; }
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) { listener.onUpdate(s.toString()); }
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }
}