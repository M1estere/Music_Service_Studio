package com.example.music_service;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.music_service.databinding.ActivityMainBinding;
import com.example.music_service.model.globals.Globs;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private CardView miniPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final boolean Set = true;

        replaceFragment(new HomeFragment());

        if (Globs.currentSongs == null) miniPlayer.setVisibility(View.GONE);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MusicPlayerViewModel musicPlayerViewModel = new MusicPlayerViewModel(this, false);

        binding.setPlayerViewModel(musicPlayerViewModel);

        binding.navBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.library_button:
                    replaceFragment(new LibraryFragment());
                    break;
                case R.id.personal_button:
                    replaceFragment(new PersonalFragment());
                    break;
                default:
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);

        fragmentTransaction.commit();
    }
}
