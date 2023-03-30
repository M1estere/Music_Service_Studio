package com.example.music_service;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QueueActivity extends AppCompatActivity {

    private RecyclerView queueRec;

    private Button backButton;

    private ArrayList<Song> songs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_activity);

        queueRec = findViewById(R.id.queue_rec_view);

        backButton = findViewById(R.id.button_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QueueActivity.super.onBackPressed();
            }
        });

        for (String title : Player.getSongs())
            createSong(title, SongsProps.ids.get(SongsProps.songs.indexOf(title)));

        QueueRecViewAdapter adapter = new QueueRecViewAdapter(this);
        adapter.setSongs(songs);

        queueRec.setAdapter(adapter);
        queueRec.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createSong(String title, int id) {
        Song songToAdd = new Song(title, id);

        songs.add(songToAdd);
    }
}
