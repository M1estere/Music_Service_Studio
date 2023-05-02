package com.example.music_service.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.music_service.R;
import com.example.music_service.databinding.ActivityUserPlaylistInfoBinding;
import com.example.music_service.viewModels.UserPlaylistInfoViewModel;

public class UserPlaylistInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserPlaylistInfoBinding binding = ActivityUserPlaylistInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_playlist_info);
        UserPlaylistInfoViewModel userPlaylistInfoViewModel = new UserPlaylistInfoViewModel(this);

        binding.setViewModel(userPlaylistInfoViewModel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}