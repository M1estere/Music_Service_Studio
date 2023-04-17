package com.example.music_service.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.music_service.R;
import com.example.music_service.databinding.ActivityMainBinding;
import com.example.music_service.fragments.HomeFragment;
import com.example.music_service.fragments.LibraryFragment;
import com.example.music_service.fragments.PersonalFragment;
import com.example.music_service.viewModels.MusicPlayerViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.music_service.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MusicPlayerViewModel musicPlayerViewModel = new MusicPlayerViewModel(this);

        binding.setViewModel(musicPlayerViewModel);

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
