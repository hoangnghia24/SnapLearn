package hcmute.edu.vn.snaplearn.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.adapters.TopicAdapter;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.viewmodels.TopicViewModel;
import hcmute.edu.vn.snaplearn.views.activities.FlashcardActivity;

public class HomeFragment extends Fragment {

    private RecyclerView rvTopics;
    private TopicAdapter topicAdapter;
    private TopicViewModel topicViewModel;
    private List<Topic> originalTopicList = new ArrayList<>();
    private EditText edtSearch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp file fragment_home.xml
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        setupViewModel();
        setupSearch();
    }

    private void initView(View view) {
        rvTopics = view.findViewById(R.id.rvTopics);
        edtSearch = view.findViewById(R.id.edtSearch);
        // 1. Khởi tạo Adapter với danh sách rỗng ban đầu (Dữ liệu sẽ được nạp từ Firestore sau)
        topicAdapter = new TopicAdapter(new ArrayList<>(), topic -> {
            Intent intent = new Intent(getActivity(), FlashcardActivity.class);
            intent.putExtra("FLASHCARD", topic);
            startActivity(intent);
        });

        // 2. Thiết lập Grid 2 cột
        rvTopics.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvTopics.setAdapter(topicAdapter);
    }

    private void setupViewModel() {
        topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);
        topicViewModel.getTopics().observe(getViewLifecycleOwner(), topics -> {
            if (topics != null) {
                originalTopicList.clear();
                originalTopicList.addAll(topics);

                // Hàm lọc tìm kiếm ta vừa tạo ở bước trước
                filterTopics(edtSearch.getText().toString());
            }
        });
    }
    private void setupSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Mỗi khi người dùng gõ hoặc xóa chữ, hàm lọc sẽ được gọi
                filterTopics(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void filterTopics(String keyword) {
        List<Topic> filteredList = new ArrayList<>();
        String lowerCaseKeyword = keyword.toLowerCase().trim();

        for (Topic topic : originalTopicList) {
            // Kiểm tra tên topic (không phân biệt hoa thường)
            if (topic.getTopicName() != null && topic.getTopicName().toLowerCase().contains(lowerCaseKeyword)) {
                filteredList.add(topic);
            }
        }

        // Đẩy danh sách đã lọc vào Adapter để RecyclerView hiển thị
        topicAdapter.updateData(filteredList);
    }
}