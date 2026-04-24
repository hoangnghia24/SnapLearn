package hcmute.edu.vn.snaplearn.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.models.User;
import hcmute.edu.vn.snaplearn.viewmodels.UserViewModel;
import hcmute.edu.vn.snaplearn.views.auth.LoginActivity;
import hcmute.edu.vn.snaplearn.views.fragments.EditProfileFragment;
import hcmute.edu.vn.snaplearn.views.fragments.HomeFragment;
import hcmute.edu.vn.snaplearn.views.fragments.LearnFragment;
import hcmute.edu.vn.snaplearn.views.fragments.QuestionFragment;
import hcmute.edu.vn.snaplearn.views.fragments.TopicFragment;

public class UserActivity extends AppCompatActivity {
    private BottomNavigationView bottom_nav_menu;
    private User currentUser;
    private UserViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        currentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        setContentView(R.layout.activity_user);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        init();
        setupObservers();
    }

    // Trong file UserActivity.java
    private void init() {
        bottom_nav_menu = findViewById(R.id.bottom_navigation);
        bottom_nav_menu.setItemIconTintList(null);

        // Mặc định hiển thị HomeFragment khi mới vào Activity
        loadFragment(new HomeFragment());

        // Nếu bạn muốn đổi Fragment khi bấm Bottom Navigation
        bottom_nav_menu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) { // Thay bằng ID menu của bạn
                loadFragment(new HomeFragment());
                return true;
            }else if (id == R.id.nav_add) {
                loadFragment(new TopicFragment());
                return true;
            }else if(id == R.id.nav_learn){
                loadFragment(new LearnFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                EditProfileFragment profileFragment = new EditProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("EDIT_USER", currentUser);
                profileFragment.setArguments(bundle);

                loadFragment(profileFragment);
                return true;
            }else if(id == R.id.nav_logout){
                viewModel.logout();
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // R.id.fragment_container là ID của FrameLayout trong layout của bạn
                .commit();
    }
    // --- BƯỚC 2: Thêm hàm này để xử lý chuyển màn hình khi đăng xuất xong ---
    private void setupObservers() {
        // Quan sát biến isLoggedOut từ UserViewModel
        viewModel.getIsLoggedOut().observe(this, isLoggedOut -> {
            if (isLoggedOut != null && isLoggedOut) {
                Toast.makeText(this, "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();

                // Chuyển hướng về màn hình Đăng nhập (Thay LoginActivity.class bằng đúng tên Activity của bạn nếu cần)
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);

                // 2 Cờ này rất quan trọng: Nó xóa toàn bộ lịch sử màn hình cũ,
                // giúp người dùng không thể bấm nút Back trên điện thoại để quay lại UserActivity khi đã đăng xuất
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish(); // Đóng Activity hiện tại
            }
        });
    }
}
