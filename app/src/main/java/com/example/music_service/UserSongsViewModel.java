package com.example.music_service;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.adapters.UserSongsRecViewAdapter;
import com.example.music_service.model.Song;

import java.util.ArrayList;

public class UserSongsViewModel extends BaseObservable {

    private View mainView;

    private ArrayList<Song> songs = new ArrayList<>();
    private RecyclerView userSongsRecView;

    public UserSongsViewModel(@NonNull View view) {
        mainView = view;

        userSongsRecView = view.findViewById(R.id.songs_rec);

        fillSongs();

        UserSongsRecViewAdapter adapter = new UserSongsRecViewAdapter(mainView.getContext());
        adapter.setSongs(songs);

        userSongsRecView.setAdapter(adapter);
        userSongsRecView.setLayoutManager(new LinearLayoutManager(mainView.getContext()));
    }

    private void fillSongs() {
        songs.add(new Song("alive.mp3", R.raw.alive));
        songs.add(new Song("animals.mp3", R.raw.animals));
        songs.add(new Song("break_through_it_all.mp3", R.raw.break_through_it_all));
        songs.add(new Song("his_world.mp3", R.raw.his_world));
    }
}
