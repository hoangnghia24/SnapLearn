package hcmute.edu.vn.snaplearn.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.adapters.TopicAdapter;
import hcmute.edu.vn.snaplearn.models.Flashcard;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.views.activities.FlashcardActivity;

public class HomeFragment extends Fragment {

    private RecyclerView rvTopics;
    private TopicAdapter topicAdapter;

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
    }
    private void initView(View view){
        // 1. Ánh xạ RecyclerView
        rvTopics = view.findViewById(R.id.rvTopics);
        List<Flashcard> flashcards = new ArrayList<>();
        flashcards.add(new Flashcard("1", "Hello", "Xin chào"));
        flashcards.add(new Flashcard("2", "Book", "Quyển sách"));

        // 2. Khởi tạo Topic bằng constructor của bạn
        Topic greetingsTopic = new Topic("T01", "GREETINGS", flashcards);
        Topic greetingsTopic1 = new Topic("T02", "GREETINGS", flashcards);
        // 2. Tạo dữ liệu mẫu (Sau này bạn sẽ lấy từ Firebase tại đây)
        List<Topic> topicList = new ArrayList<>();
        topicList.add(greetingsTopic);
        topicList.add(greetingsTopic1);
        // 3. Khởi tạo Adapter
        topicAdapter = new TopicAdapter(topicList, topic -> {
            Intent intent = new Intent(getActivity(), FlashcardActivity.class);
            intent.putExtra("FLASHCARD",topic);
            startActivity(intent);
        });
        // 4. Thiết lập Grid 2 cột
        rvTopics.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvTopics.setAdapter(topicAdapter);
    }
}