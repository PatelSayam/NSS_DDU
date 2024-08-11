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

import com.example.nss_ddu.R;
import com.example.nss_ddu.databinding.FragmentSplashBinding;

import com.example.nss_ddu.databinding.ActivityMainBinding;

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

        //animations
        Animation fade_in = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        Animation scale_up = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up);

        binding.applogo.startAnimation(scale_up);
        binding.appQuoate.startAnimation(fade_in);

        // Navigate to LoginFragment after 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_splashFragment_to_loginFragment);
            }
        }, 3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}