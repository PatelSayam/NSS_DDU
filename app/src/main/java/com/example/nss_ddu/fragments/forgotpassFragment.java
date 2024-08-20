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
import com.example.nss_ddu.databinding.FragmentForgotpassBinding;

public class forgotpassFragment extends Fragment {

    private FragmentForgotpassBinding binding;
    private Cognito cognito;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotpassBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cognito = new Cognito(getContext());

        binding.submitbtn.setOnClickListener(v -> handleForgotPassword());
    }

    private void handleForgotPassword() {
        String email = binding.email.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.email.setError("Email is required");
            return;
        }

        cognito.forgotPassword(email, new Cognito.ForgotPasswordCallback() {
            @Override
            public void onSuccess() {
                Log.d("ForgotPass", "onSuccess: Reset code sent. Navigating to confirm password fragment.");
                showToast("Reset code sent to your email");

                // Navigate to confirm password fragment
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_forgotPassFragment_to_confirmpassFragment);
            }

            @Override
            public void onFailure(Exception exception) {
                showToast("Failed to send reset code: " + exception.getMessage());
                Log.d("ForgotPass", "Failed to send reset code: " + exception.getMessage(), exception);
            }
        },this);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}

