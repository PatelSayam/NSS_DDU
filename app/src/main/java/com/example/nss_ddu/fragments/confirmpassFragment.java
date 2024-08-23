package com.example.nss_ddu.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.nss_ddu.Cognito;
import com.example.nss_ddu.R;
import com.example.nss_ddu.databinding.FragmentConfirmpassBinding;

public class confirmpassFragment extends Fragment {

    private FragmentConfirmpassBinding binding;
    private Cognito cognito;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfirmpassBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cognito = new Cognito(getContext());

        binding.submitbtn.setOnClickListener(v -> handlePasswordReset());
    }

    private void handlePasswordReset() {
        String email = binding.email.getText().toString().trim();
        String resetCode = binding.otp.getText().toString().trim();
        String newPassword = binding.password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.email.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(resetCode)) {
            binding.otp.setError("Reset code is required");
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            binding.password.setError("New password is required");
            return;
        }

        Log.d("ConfirmPass", "Email: " + email + ", ResetCode: " + resetCode + ", NewPassword: " + newPassword);

        cognito.confirmForgotPassword(email, resetCode, newPassword, new Cognito.ConfirmPasswordCallback() {
            public void onSuccess1() {
                Log.d("confirmPass","password reset successful");
                showToast("Password reset successful");
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_confirmpassFragment_to_loginFragment);
            }

            @Override
            public void onFailure1(Exception exception) {
                showToast("Password reset failed: " + exception.getMessage());
                Log.e("ConfirmPass", "Password reset failed", exception);
            }
        }, this);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
