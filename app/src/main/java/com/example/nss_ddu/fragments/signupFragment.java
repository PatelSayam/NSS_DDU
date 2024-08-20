package com.example.nss_ddu.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.nss_ddu.Cognito;
import com.example.nss_ddu.R;
import com.example.nss_ddu.databinding.FragmentSignupBinding;

public class signupFragment extends Fragment {

    private FragmentSignupBinding binding;
    private Cognito authentication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Cognito
        authentication = new Cognito(getContext());

        // Set onClick listeners for buttons
        binding.signupbtn.setOnClickListener(v -> handleSignUp());
        binding.login.setOnClickListener(v -> navigateToLogin());
    }

    private void handleSignUp() {
        String name = binding.name.getText().toString().trim();
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        String phoneNumber = "+91"+binding.phonenumber.getText().toString().trim();
        String branch = binding.branch.getText().toString().trim();
        String semester = binding.semester.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(name)) {
            binding.name.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            binding.email.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.password.setError("Password is required");
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            binding.phonenumber.setError("Phone number is required");
            return;
        }
        if (TextUtils.isEmpty(branch)) {
            binding.branch.setError("Branch is required");
            return;
        }
        if (TextUtils.isEmpty(semester)) {
            binding.semester.setError("Semester is required");
            return;
        }

        // Add attributes to Cognito
        authentication.addAttribute("name", name);
        authentication.addAttribute("email", email);
        authentication.addAttribute("phone_number", phoneNumber);
        authentication.addAttribute("custom:branch", branch);  // Custom attribute
        authentication.addAttribute("custom:semester", semester);  // Custom attribute

        // Sign up the user
        authentication.signUpInBackground(email, password);
        promptForOtp(email);
    }

    private void navigateToLogin() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_signupFragment_to_loginFragment);
    }

    private void promptForOtp(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter OTP");

        final EditText otpInput = new EditText(getContext());
        otpInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(otpInput);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String otpCode = otpInput.getText().toString().trim();
            if (!TextUtils.isEmpty(otpCode)) {
                authentication.confirmUser(email, otpCode);
            } else {
                Toast.makeText(getContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
