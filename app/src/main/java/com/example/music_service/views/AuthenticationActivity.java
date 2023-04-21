package com.example.music_service.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.music_service.R;
import com.example.music_service.viewModels.AuthenticationActivityViewModel;

public class AuthenticationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        AuthenticationActivityViewModel authenticationActivityViewModel = new AuthenticationActivityViewModel(this);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}