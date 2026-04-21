package hcmute.edu.vn.snaplearn.views.activities;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.adapters.FlashcardAdapter;
import hcmute.edu.vn.snaplearn.models.Flashcard;
import hcmute.edu.vn.snaplearn.models.Topic;

public class FlashcardActivity extends AppCompatActivity {
    private ProgressBar pbLearning;
    private TextView tvProgressText;
    // Giả sử Nghĩa đã nhận topic từ Intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        Topic topic = (Topic) getIntent().getSerializableExtra("FLASHCARD");
        initView(topic);
    }
    private void initView(Topic topic){
        tvProgressText = findViewById(R.id.tvProgressText);
        pbLearning = findViewById(R.id.pbLearning);
        ViewPager2 vpFlashcards = findViewById(R.id.vpFlashcards);
        if (topic != null) {
            List<Flashcard> list = topic.getFlashcardList();
            FlashcardAdapter adapter = new FlashcardAdapter(list);
            vpFlashcards.setAdapter(adapter);

            // Lắng nghe sự kiện vuốt để cập nhật ProgressBar
            vpFlashcards.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    // Cập nhật Progress (ví dụ: 1/10, 2/10...)
                    int current = position + 1;
                    tvProgressText.setText(current + "/" + list.size());
                    pbLearning.setProgress((current * 100) / list.size());
                }
            });
            setTitle(topic.getTopicName());
        }
    }
}
