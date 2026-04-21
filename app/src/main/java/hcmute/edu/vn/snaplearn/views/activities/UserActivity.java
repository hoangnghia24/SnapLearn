package hcmute.edu.vn.snaplearn.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.views.fragments.HomeFragment;
import hcmute.edu.vn.snaplearn.views.fragments.TopicFragment;

public class UserActivity extends AppCompatActivity {
    private BottomNavigationView bottom_nav_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_user);
        init();
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
}
