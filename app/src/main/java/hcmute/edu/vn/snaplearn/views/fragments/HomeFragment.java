package hcmute.edu.vn.snaplearn.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.adapters.TopicAdapter;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.viewmodels.HomeViewModel;
import hcmute.edu.vn.snaplearn.views.activities.FlashcardActivity;

public class HomeFragment extends Fragment {

    private RecyclerView rvTopics;
    private TopicAdapter topicAdapter;
    private HomeViewModel homeViewModel; // Khai báo ViewModel

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
        setupViewModel(); // Gọi hàm thiết lập ViewModel
    }

    private void initView(View view) {
        rvTopics = view.findViewById(R.id.rvTopics);

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
        // 1. Khởi tạo ViewModel gắn với vòng đời của Fragment này
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // 2. Lắng nghe dữ liệu từ Firestore thông qua LiveData
        homeViewModel.getTopics().observe(getViewLifecycleOwner(), topics -> {
            if (topics != null) {
                // Khi có dữ liệu từ Firebase trả về, cập nhật lên giao diện
                topicAdapter.updateData(topics);
            }
        });
    }
}