package com.example.music_service.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.music_service.R;
import com.example.music_service.databinding.ActivitySearchBinding;
import com.example.music_service.viewModels.SearchViewModel;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        searchViewModel = new SearchViewModel(this);

        binding.setViewModel(searchViewModel);
    }
}