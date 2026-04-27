package hcmute.edu.vn.snaplearn.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.adapters.FlashcardEditAdapter;
import hcmute.edu.vn.snaplearn.models.Flashcard;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.viewmodels.TopicViewModel; // Import ViewModel
import hcmute.edu.vn.snaplearn.views.activities.ScannerActivity;

public class TopicFragment extends Fragment {

    private RecyclerView rvEditFlashcards;
    private FloatingActionButton fabAddCard;
    private EditText edtTopicName; // Thay TextView thành EditText
    private ImageButton btnDone;

    private FlashcardEditAdapter adapter;
    private List<Flashcard> listEditCards;
    private Topic editingTopic;
    private LinearLayout layoutScanCamera;
    private TopicViewModel topicViewModel; // Khai báo ViewModel

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ViewModel
        topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);

        initViews(view);
        initData();
        setupRecyclerView();
        setupListeners();
        setupSwipeToDelete();
    }

    private void initViews(View view) {
        rvEditFlashcards = view.findViewById(R.id.rvEditFlashcards);
        fabAddCard = view.findViewById(R.id.fabAddCard);
        edtTopicName = view.findViewById(R.id.edtTopicName); // Ánh xạ EditText
        btnDone = view.findViewById(R.id.btnDone);
        layoutScanCamera = view.findViewById(R.id.layoutScanCamera);
    }

    private void initData() {
        listEditCards = new ArrayList<>();

        if (getArguments() != null && getArguments().containsKey("edit_topic")) {
            editingTopic = (Topic) getArguments().getSerializable("edit_topic");

            if (editingTopic != null) {
                // Hiển thị tên chủ đề cũ lên EditText
                edtTopicName.setText(editingTopic.getTopicName());

                if (editingTopic.getFlashcardList() != null) {
                    listEditCards.addAll(editingTopic.getFlashcardList());
                }
            }
        } else {
            // Chế độ tạo mới: Xóa trống ô nhập tên
            edtTopicName.setText("");
        }

        if (listEditCards.isEmpty()) {
            listEditCards.add(new Flashcard("", "", ""));
        }
    }
    private final ActivityResultLauncher<Intent> scanLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String wordEn = data.getStringExtra("SCANNED_WORD_EN");
                    String wordVi = data.getStringExtra("TRANSLATED_WORD_VI");
                    String phonetic = data.getStringExtra("PHONETIC_TEXT");

                    if (wordEn != null && !wordEn.isEmpty()) {
                        // Nếu danh sách chỉ có 1 thẻ rỗng (mặc định khi tạo mới), thì xóa nó đi
                        if (listEditCards.size() == 1 && listEditCards.get(0).getWordEn().isEmpty()) {
                            listEditCards.clear();
                        }

                        // Thêm thẻ mới với dữ liệu quét được
                        // Giả sử constructor của bạn là: Flashcard(wordEn, wordVi, phonetic)
                        listEditCards.add(new Flashcard(wordEn, wordVi != null ? wordVi : "", phonetic != null ? phonetic : ""));

                        // Cập nhật giao diện
                        adapter.notifyDataSetChanged();
                        rvEditFlashcards.smoothScrollToPosition(listEditCards.size() - 1);
                        Toast.makeText(getContext(), "Đã thêm thẻ từ Camera!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private void setupRecyclerView() {
        adapter = new FlashcardEditAdapter(listEditCards);
        rvEditFlashcards.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvEditFlashcards.setAdapter(adapter);
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void setupListeners() {
        fabAddCard.setOnClickListener(v -> {
            listEditCards.add(new Flashcard("", "", ""));
            int newPosition = listEditCards.size() - 1;
            adapter.notifyItemInserted(newPosition);
            rvEditFlashcards.smoothScrollToPosition(newPosition);
        });
        layoutScanCamera.setOnClickListener(v -> {
            // Mở màn hình ScannerActivity bằng scanLauncher
            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            scanLauncher.launch(intent);
        });
        // ========== XỬ LÝ NÚT LƯU (CẬP NHẬT / TẠO MỚI) ==========
        btnDone.setOnClickListener(v -> {
            // 1. Validate Tên chủ đề
            String topicName = edtTopicName.getText().toString().trim();
            if (topicName.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tên chủ đề!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Validate danh sách Flashcard
            boolean isValid = true;
            for (Flashcard card : listEditCards) {
                if (card.getWordEn().trim().isEmpty() || card.getWordVi().trim().isEmpty()) {
                    isValid = false;
                    break;
                }
            }

            if (!isValid) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ Tiếng Anh và Dịch nghĩa cho tất cả các thẻ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. Logic Lưu / Cập nhật dữ liệu bằng ViewModel
            if (editingTopic != null) {
                // ĐANG SỬA CHỦ ĐỀ CŨ
                editingTopic.setTopicName(topicName);
                editingTopic.setFlashcardList(listEditCards);

                // Gọi ViewModel để cập nhật
                topicViewModel.updateTopic(editingTopic);
                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                Bundle result = new Bundle();
                result.putSerializable("updated_topic", editingTopic);
                getParentFragmentManager().setFragmentResult("edit_request", result);
            } else {
                // ĐANG TẠO CHỦ ĐỀ MỚI
                Topic newTopic = new Topic("", topicName, listEditCards);

                // Gọi ViewModel để tạo mới
                topicViewModel.addTopic(newTopic);
                Toast.makeText(getContext(), "Tạo chủ đề thành công!", Toast.LENGTH_SHORT).show();
            }

            // 4. Đóng Fragment
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                Flashcard deletedCard = listEditCards.get(position);

                listEditCards.remove(position);
                adapter.notifyItemRemoved(position);

                showUndoSnackbar(deletedCard, position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvEditFlashcards);
    }

    private void showUndoSnackbar(Flashcard deletedCard, int position) {
        Snackbar snackbar = Snackbar.make(rvEditFlashcards, "Đã xóa 1 thẻ từ vựng", Snackbar.LENGTH_LONG);
        snackbar.setAction("HOÀN TÁC", v -> {
            listEditCards.add(position, deletedCard);
            adapter.notifyItemInserted(position);
            rvEditFlashcards.scrollToPosition(position);
        });
        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_orange_light));
        snackbar.show();
    }
}