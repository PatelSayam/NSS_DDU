package com.example.nss_ddu.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.NavOptions;

import com.example.nss_ddu.R;
import com.example.nss_ddu.databinding.FragmentSplashBinding;

public class splashFragment extends Fragment {
    private FragmentSplashBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation fade_in= AnimationUtils.loadAnimation(requireContext(),R.anim.fade_in);
        Animation scale_up= AnimationUtils.loadAnimation(requireContext(),R.anim.scale_up);
        binding.appquote.startAnimation(fade_in);
        binding.applogo.startAnimation(scale_up);
        // Navigate to LoginFragment after 3 seconds
        new Handler().postDelayed(() -> {
            NavController navController = Navigation.findNavController(view);

            // Create NavOptions to avoid adding SplashFragment to the back stack
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true) // Pop SplashFragment from back stack
                    .build();

            navController.navigate(R.id.action_splashFragment_to_loginFragment, null, navOptions);
        }, 3000); // 3000 milliseconds = 3 seconds
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
