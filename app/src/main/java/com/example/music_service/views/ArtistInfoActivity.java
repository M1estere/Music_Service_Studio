package com.example.music_service.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.music_service.R;
import com.example.music_service.databinding.ActivityArtistInfoBinding;
import com.example.music_service.viewModels.ArtistInfoViewModel;

public class ArtistInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityArtistInfoBinding binding = ActivityArtistInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_artist_info);
        ArtistInfoViewModel artistInfoViewModel = new ArtistInfoViewModel(this);

        binding.setViewModel(artistInfoViewModel);
    }
}