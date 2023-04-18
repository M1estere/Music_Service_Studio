package com.example.music_service.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.music_service.R;
import com.example.music_service.databinding.ActivityPlaylistInfoBinding;
import com.example.music_service.viewModels.PlaylistInfoViewModel;

public class PlaylistInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPlaylistInfoBinding binding = ActivityPlaylistInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_playlist_info);
        PlaylistInfoViewModel playlistInfoViewModel = new PlaylistInfoViewModel(this);

        binding.setViewModel(playlistInfoViewModel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}