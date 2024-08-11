package com.example.nss_ddu.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.nss_ddu.R;
import com.example.nss_ddu.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding; // Use the correct binding type

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false); // Inflate the correct layout
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up button click listener
        binding.loginbtn.setOnClickListener(v -> handleLogin());

        // Set up sign-up and forgot password click listeners
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

        // Implement your login logic here
        // For demonstration purposes, we'll just show a toast
        Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();

        // Navigate to another fragment or activity after successful login
        // Example: navigate to the next screen
        // NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        // navController.navigate(R.id.action_loginFragment_to_nextFragment);
    }

    private void navigateToSignUp() {
        // Navigate to the sign-up fragment
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_loginFragment_to_signupFragment); // Update with actual action ID
    }

    private void navigateToForgotPassword() {
        // Navigate to the forgot password fragment
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment); // Update with actual action ID
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
