package com.example.music_service.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.music_service.R;
import com.example.music_service.databinding.ActivityMainBinding;
import com.example.music_service.fragments.HomeFragment;
import com.example.music_service.fragments.LibraryFragment;
import com.example.music_service.fragments.PersonalFragment;
import com.example.music_service.viewModels.MusicPlayerViewModel;

public class MainActivity extends AppCompatActivity {

    private MusicPlayerViewModel musicPlayerViewModel;
    private int currentFragment = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.music_service.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        musicPlayerViewModel = new MusicPlayerViewModel(this);

        binding.setViewModel(musicPlayerViewModel);

        binding.navBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    if (currentFragment == 0) return true;
                    currentFragment = 0;
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.library_button:
                    if (currentFragment == 1) return true;
                    currentFragment = 1;
                    replaceFragment(new LibraryFragment());
                    break;
                case R.id.personal_button:
                    if (currentFragment == 2) return true;
                    currentFragment = 2;
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
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        musicPlayerViewModel.updateUI();
    }

    @Override
    public void onBackPressed() {
    }
}
