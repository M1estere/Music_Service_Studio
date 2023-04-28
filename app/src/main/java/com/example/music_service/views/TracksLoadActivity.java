package com.example.music_service.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.music_service.R;
import com.example.music_service.models.data.DataLoader;
import com.example.music_service.models.globals.Globs;

public class TracksLoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_load);

        loadTracksFromFirebaseStorage();
    }

    private void loadTracksFromFirebaseStorage() {
        DataLoader.fillSongsFromDrive(this);
        openMainPage();
    }

    private void openMainPage() {
        Globs.recheckLogin = false;

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}