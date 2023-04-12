package com.example.music_service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AuthenticationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        AuthenticationActivityViewModel authenticationActivityViewModel = new AuthenticationActivityViewModel(this);
    }
}