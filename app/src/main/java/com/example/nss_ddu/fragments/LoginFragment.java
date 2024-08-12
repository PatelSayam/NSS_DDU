package com.example.nss_ddu.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.nss_ddu.Cognito;
import com.example.nss_ddu.R;
import com.example.nss_ddu.databinding.FragmentLoginBinding;

import java.util.Map;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private Cognito authentication;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginbtn.setOnClickListener(v -> handleLogin());

        binding.signup.setOnClickListener(v -> navigateToSignUp());
        binding.forgotpass.setOnClickListener(v -> navigateToForgotPassword());
    }

    private void handleLogin() {
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.email.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.password.setError("Password is required");
            return;
        }
        Cognito authentication = new Cognito(getContext());
        authentication.userLogin(email.replace(" ", ""), password);
    }

    private void navigateToSignUp() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_loginFragment_to_signupFragment);
    }

    private void navigateToForgotPassword() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
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
