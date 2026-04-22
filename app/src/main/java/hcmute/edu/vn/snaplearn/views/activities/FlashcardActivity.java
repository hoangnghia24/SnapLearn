package hcmute.edu.vn.snaplearn.views.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.adapters.FlashcardAdapter;
import hcmute.edu.vn.snaplearn.models.Flashcard;
import hcmute.edu.vn.snaplearn.models.Topic;
import hcmute.edu.vn.snaplearn.viewmodels.TopicViewModel;
import hcmute.edu.vn.snaplearn.views.fragments.TopicFragment;

public class FlashcardActivity extends AppCompatActivity {
    private ProgressBar pbLearning;
    private TextView tvProgressText;
    private ImageButton btnMoreOptions;
    private TextView tvTopicTitle;
    private ViewPager2 vpFlashcards;

    private Topic currentTopic;
    private TopicViewModel topicViewModel; // Khai báo ViewModel để gọi lệnh Xóa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // Khởi tạo ViewModel
        topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);

        // Lấy dữ liệu ban đầu từ Intent
        currentTopic = (Topic) getIntent().getSerializableExtra("FLASHCARD");
        initView(currentTopic);
        setupMoreOptions();

        // LẮNG NGHE SỰ KIỆN TỪ FRAGMENT TRẢ VỀ SAU KHI SỬA XONG
        getSupportFragmentManager().setFragmentResultListener("edit_request", this, (requestKey, bundle) -> {
            Topic updatedTopic = (Topic) bundle.getSerializable("updated_topic");
            if (updatedTopic != null) {
                currentTopic = updatedTopic; // Cập nhật lại biến hiện tại
                initView(currentTopic);      // Gọi lại hàm vẽ giao diện với dữ liệu mới
            }
        });
    }

    private void initView(Topic topic){
        tvProgressText = findViewById(R.id.tvProgressText);
        pbLearning = findViewById(R.id.pbLearning);
        vpFlashcards = findViewById(R.id.vpFlashcards);
        btnMoreOptions = findViewById(R.id.btnMoreOptions);
        tvTopicTitle = findViewById(R.id.tvTopicTitle);

        if (topic != null) {
            tvTopicTitle.setText(topic.getTopicName());

            List<Flashcard> list = topic.getFlashcardList();
            FlashcardAdapter adapter = new FlashcardAdapter(list);
            vpFlashcards.setAdapter(adapter);

            // Reset ProgressBar về ban đầu khi nạp lại dữ liệu
            tvProgressText.setText("1/" + list.size());
            pbLearning.setProgress((1 * 100) / list.size());

            // Xóa hết các listener cũ (nếu có) trước khi tạo mới để tránh lỗi lặp
            vpFlashcards.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    int current = position + 1;
                    tvProgressText.setText(current + "/" + list.size());
                    pbLearning.setProgress((current * 100) / list.size());
                }
            });
        }
    }

    private void setupMoreOptions() {
        if (btnMoreOptions == null) return;

        btnMoreOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(FlashcardActivity.this, v);
            popupMenu.getMenu().add("Sửa chủ đề");
            popupMenu.getMenu().add("Xóa chủ đề");

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Sửa chủ đề")) {
                    openEditTopicFragment();
                    return true;
                } else if (item.getTitle().equals("Xóa chủ đề")) {
                    confirmDeleteTopic();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    private void openEditTopicFragment() {
        if (currentTopic == null) return;

        TopicFragment editFragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("edit_topic", currentTopic);
        editFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, editFragment)
                .addToBackStack(null)
                .commit();
    }

    // HOÀN THIỆN CHỨC NĂNG XÓA
    private void confirmDeleteTopic() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa chủ đề")
                .setMessage("Bạn có chắc chắn muốn xóa chủ đề này cùng các flashcard bên trong không? Hành động này không thể hoàn tác.")
                .setPositiveButton("Xóa", (dialog, which) -> {

                    // Gọi ViewModel để xóa khỏi Firebase
                    if (currentTopic != null && currentTopic.getTopicId() != null) {
                        topicViewModel.deleteTopic(currentTopic.getTopicId());
                    }

                    Toast.makeText(FlashcardActivity.this, "Đã xóa chủ đề!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình Activity, tự động quay về HomeFragment
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}