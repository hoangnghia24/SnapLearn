package hcmute.edu.vn.snaplearn.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.models.Topic;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> topicList;
    private OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onTopicClick(Topic topic);
    }

    public TopicAdapter(List<Topic> topicList, OnTopicClickListener listener) {
        this.topicList = topicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);

        holder.tvTopicName.setText(topic.getTopicName());

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTopicClick(topic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }
    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopicName;
        ImageView imgTopicIcon;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopicName = itemView.findViewById(R.id.tvTopicName);
            imgTopicIcon = itemView.findViewById(R.id.imgTopicIcon);
        }
    }
    // Đặt hàm này bên trong class TopicAdapter
    public void updateData(List<Topic> newTopics) {
        this.topicList.clear(); // Xóa dữ liệu cũ
        this.topicList.addAll(newTopics); // Thêm dữ liệu mới từ Firestore
        notifyDataSetChanged(); // Yêu cầu RecyclerView vẽ lại giao diện
    }
}