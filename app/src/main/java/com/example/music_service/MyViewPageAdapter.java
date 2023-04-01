package com.example.music_service;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class MyViewPageAdapter extends FragmentStateAdapter {

    public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UserPlaylistsFragment();
            case 1:
                return new UserSongsFragment();
            case 2:
                return new UserAlbumsFragment();
            default:
                return new UserSongsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
