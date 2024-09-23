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

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.example.nss_ddu.Cognito;
import com.example.nss_ddu.R;
import com.example.nss_ddu.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private Cognito authentication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Cognito authentication
        authentication = new Cognito(getContext());

        // Set login button click listener
        binding.loginbtn.setOnClickListener(v -> handleLogin());

        // Navigate to signup page
        binding.signup.setOnClickListener(v -> navigateToSignUp());

        // Navigate to forgot password page
        binding.forgotpass.setOnClickListener(v -> navigateToForgotPassword());
    }

    // Handle the login action
    private void handleLogin() {
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        // Validate email input
        if (TextUtils.isEmpty(email)) {
            binding.email.setError("Email is required");
            return;
        }

        // Validate password input
        if (TextUtils.isEmpty(password)) {
            binding.password.setError("Password is required");
            return;
        }

        // Perform Cognito login
        authentication.userLogin(email.replace(" ", ""), password, new Cognito.LoginCallback() {
            @Override
            public void onSuccess(CognitoUserSession session) {
                showToast("Login successful");

                // Navigate to HomeFragment without fetching events here
                navigateToHome();
            }

            @Override
            public void onFailure(Exception exception) {
                showToast("Login failed: " + exception.getMessage());
                // Clear only the password field for better UX
                binding.password.setText("");
            }
        });
    }

    // Navigate to HomeFragment
    private void navigateToHome() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_loginFragment_to_homeFragment);
    }

    // Navigate to SignUpFragment
    private void navigateToSignUp() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_loginFragment_to_signupFragment);
    }

    // Navigate to ForgotPasswordFragment
    private void navigateToForgotPassword() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
    }

    // Show a toast message
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
