package hcmute.edu.vn.snaplearn.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.viewmodels.auth.LoginViewModel;
import hcmute.edu.vn.snaplearn.views.activities.ForgotPasswordActivity;
import hcmute.edu.vn.snaplearn.views.activities.UserActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUserNameGmail;
    private EditText edtPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private LoginViewModel loginViewModel;
    private Button btnRegister;
    private TextView tvForgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setupViewModel();
        setupListener();
    }
    private void initViews() {
        edtUserNameGmail = findViewById(R.id.edt_username_gmail);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.pb_loading);
        btnRegister = findViewById(R.id.btn_register_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
    }
    private void setupViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? ProgressBar.VISIBLE : ProgressBar.GONE);
            btnLogin.setEnabled(!isLoading);
        });

        loginViewModel.getToastMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // LẮNG NGHE OBJECT USER TRẢ VỀ TỪ VIEWMODEL
        loginViewModel.getLoggedInUser().observe(this, user -> {
            if (user != null) {
                Intent intent = new Intent(LoginActivity.this, UserActivity.class);

                // Nhét dữ liệu user thật vào Intent
                intent.putExtra("CURRENT_USER", user);

                startActivity(intent);
                finish(); // Đóng màn hình đăng nhập
            }
        });
    }
    private void setupListener(){
        btnLogin.setOnClickListener(v -> {
            String userNameGmail = edtUserNameGmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            loginViewModel.login(userNameGmail, password);});
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);});
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);});
    }
}
