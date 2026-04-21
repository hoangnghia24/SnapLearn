package hcmute.edu.vn.snaplearn.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hcmute.edu.vn.snaplearn.R;

public class UserActivity extends AppCompatActivity {
    private BottomNavigationView bottom_nav_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_user);
        init();
    }

    private void init(){
        bottom_nav_menu = findViewById(R.id.bottom_navigation);
        bottom_nav_menu.setItemIconTintList(null);
    }
}
