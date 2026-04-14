package hcmute.edu.vn.snaplearn.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.viewmodels.auth.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel viewModel;
    private EditText edtFullName;
    private EditText edtGmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private AppCompatButton btnRegister;
    private ProgressBar progressBar;
    private ImageView ivArrowBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        setupViewModel();
        setupListener();
    }
    private void initViews() {
        edtFullName = findViewById(R.id.edt_full_name);
        edtGmail = findViewById(R.id.edt_gmail);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.pb_loading);
        ivArrowBack = findViewById(R.id.iv_arrow_back);
    }
    private void setupViewModel() {
        // Khởi tạo ViewModel đúng chuẩn của Android Architecture Components
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // --- BẮT ĐẦU QUAN SÁT (OBSERVE) CÁC LIVEDATA ---

        // 1. Lắng nghe vòng xoay Loading
        viewModel.getIsLoading().observe(this, isLoading -> {
            // Hiện vòng xoay nếu đang load, ẩn đi nếu load xong
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);

            // UX Tip: Khóa nút đăng ký lại để tránh người dùng bấm liên tục gây lỗi
            btnRegister.setEnabled(!isLoading);
        });

        // 2. Lắng nghe thông báo (Lỗi hoặc Thành công)
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // 3. Lắng nghe sự kiện chuyển màn hình
        viewModel.getIsRegisterSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                // Đăng ký thành công thì chuyển sang màn hình Đăng nhập (hoặc Màn hình chính)
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                // Cờ này giúp xóa màn hình Đăng ký khỏi lịch sử,
                // để khi vào Login, người dùng bấm nút Back sẽ thoát app chứ không lùi lại màn hình Đăng ký.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish(); // Đóng Activity hiện tại
            }
        });
    }
    private void setupListener(){
        btnRegister.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String gmail = edtGmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();
            viewModel.register(gmail, fullName, password, confirmPassword);
        });
        ivArrowBack.setOnClickListener(v -> finish());
    }
}
