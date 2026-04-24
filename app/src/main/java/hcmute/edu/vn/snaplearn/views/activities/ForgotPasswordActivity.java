package hcmute.edu.vn.snaplearn.views.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.viewmodels.auth.ForgotPasswordViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edtGmailForgotpassword;
    private Button btnSendCode;

    private ForgotPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();

        // Khởi tạo ViewModel cực kỳ đơn giản (không cần Factory)
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        setupObservers();
        setupListeners();
    }

    private void initViews() {
        edtGmailForgotpassword = findViewById(R.id.edt_gmail_forgotpassword);
        btnSendCode = findViewById(R.id.btn_send_code);
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            btnSendCode.setEnabled(!isLoading);
            btnSendCode.setText(isLoading ? "Đang gửi..." : "Gửi mã");
        });

        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getIsSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                finish(); // Đóng Activity và quay lại màn hình đăng nhập
            }
        });
    }

    private void setupListeners() {
        btnSendCode.setOnClickListener(v -> {
            String email = edtGmailForgotpassword.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                edtGmailForgotpassword.setError("Vui lòng nhập email của bạn!");
                edtGmailForgotpassword.requestFocus();
                return;
            }
            viewModel.resetPassword(email);
        });
    }
}