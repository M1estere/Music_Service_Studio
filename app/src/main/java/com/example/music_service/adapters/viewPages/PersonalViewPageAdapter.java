package com.example.music_service.adapters.viewPages;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music_service.fragments.UserPlaylistsFragment;
import com.example.music_service.fragments.UserSongsFragment;

public class PersonalViewPageAdapter extends FragmentStateAdapter {

    public PersonalViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new UserPlaylistsFragment();
        }
        return new UserSongsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
