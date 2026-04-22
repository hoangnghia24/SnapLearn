package hcmute.edu.vn.snaplearn.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import hcmute.edu.vn.snaplearn.R;
import hcmute.edu.vn.snaplearn.models.User;
import hcmute.edu.vn.snaplearn.viewmodels.UserViewModel;

public class EditProfileFragment extends Fragment {

    private EditText edtEmail, edtFullName;
    private AppCompatButton btnSaveProfile;
    private ImageButton btnCloseProfile;

    private UserViewModel userViewModel;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupViewModel();
        loadUserData();
        setupListeners();
    }

    private void initViews(View view) {
        edtEmail = view.findViewById(R.id.edtEmail);
        edtFullName = view.findViewById(R.id.edtFullName);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        btnCloseProfile = view.findViewById(R.id.btnCloseProfile);
    }

    private void setupViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Lắng nghe kết quả từ ViewModel
        userViewModel.getUpdateStatus().observe(getViewLifecycleOwner(), status -> {
            if ("SUCCESS".equals(status)) {
                Toast.makeText(getContext(), "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack(); // Quay lại trang trước
            } else {
                Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserData() {
        if (getArguments() != null) {
            // Lấy object User được truyền sang từ ProfileFragment (hoặc màn hình trước đó)
            // Nhớ thêm "implements Serializable" vào class User của bạn nhé!
            currentUser = (User) getArguments().getSerializable("EDIT_USER");

            if (currentUser != null) {
                edtEmail.setText(currentUser.getGmail());
                edtFullName.setText(currentUser.getFullName());
            }
        }
    }

    private void setupListeners() {
        btnCloseProfile.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        btnSaveProfile.setOnClickListener(v -> {
            String newName = edtFullName.getText().toString().trim();

            if (newName.isEmpty()) {
                Toast.makeText(getContext(), "Tên không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentUser != null) {
                // Cập nhật tên mới vào object và gửi đi
                currentUser.setFullName(newName);
                userViewModel.updateUserInfo(currentUser);
            }
        });
    }
}