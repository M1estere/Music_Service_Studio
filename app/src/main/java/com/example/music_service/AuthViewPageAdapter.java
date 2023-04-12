package com.example.music_service;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AuthViewPageAdapter extends FragmentStateAdapter {
    public AuthViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        System.out.printf("Chosen %d\n", position);

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