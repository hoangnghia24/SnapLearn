package hcmute.edu.vn.snaplearn.views.fragments;

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
import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.adapters.TopicAdapter;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.repositories.TopicRepository;
import hcmute.edu.vn.snaplearn.viewmodels.TopicViewModel;

public class LearnFragment extends Fragment {

    private RecyclerView rvTopicsLearn;
    private TopicAdapter topicAdapter;
    private List<Topic> topicList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_learn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTopicsLearn = view.findViewById(R.id.rvTopicsLearn);
        setupRecyclerView();
    }

    // Trong hàm setupRecyclerView() của LearnFragment.java

    private void setupRecyclerView() {
        topicList = new ArrayList<>();
        rvTopicsLearn.setLayoutManager(new GridLayoutManager(getContext(), 2));

        topicAdapter = new TopicAdapter(topicList, topic -> {
            QuestionFragment questionFragment = new QuestionFragment();

            // Truyền TOPIC_ID sang cho QuestionFragment
            Bundle bundle = new Bundle();
            bundle.putString("TOPIC_ID", topic.getTopicId());
            questionFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, questionFragment)
                    .addToBackStack(null)
                    .commit();
        });

        rvTopicsLearn.setAdapter(topicAdapter);

        // CHUẨN MVVM: Khởi tạo ViewModel và observe dữ liệu
        TopicViewModel topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);

        topicViewModel.getTopics().observe(getViewLifecycleOwner(), topics -> {
            if (topics != null) {
                topicAdapter.updateData(topics);
            }
        });
    }
}