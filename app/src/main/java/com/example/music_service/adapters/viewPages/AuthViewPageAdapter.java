package com.example.music_service.adapters.viewPages;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music_service.fragments.LoginFragment;
import com.example.music_service.fragments.RegistrationFragment;

public class AuthViewPageAdapter extends FragmentStateAdapter {
    public AuthViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new LoginFragment();
        }

        return new RegistrationFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}