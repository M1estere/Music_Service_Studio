package com.example.music_service;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.music_service.databinding.PlayerActivityBinding;

public class MusicPlayerActivity extends AppCompatActivity {

    private PlayerActivityBinding playerActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

        playerActivityBinding = DataBindingUtil.setContentView(this, R.layout.player_activity);
        MusicPlayerViewModel musicPlayerViewModel = new MusicPlayerViewModel(this, true);

        playerActivityBinding.setViewModel(musicPlayerViewModel);
    }
}
