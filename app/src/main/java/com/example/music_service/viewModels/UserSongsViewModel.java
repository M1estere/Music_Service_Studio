package com.example.music_service.viewModels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.UserSongsRecViewAdapter;
import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.models.Song;
import com.example.music_service.models.globals.SongsProps;

import java.util.ArrayList;

public class UserSongsViewModel extends BaseObservable {

    private View mainView;

    private ArrayList<Song> songs = new ArrayList<>();
    private RecyclerView userSongsRecView;
    private UserSongsRecViewAdapter adapter;

    public UserSongsViewModel(@NonNull View view) {
        FavouriteMusic.setUserSongsViewModel(this);
        mainView = view;

        userSongsRecView = view.findViewById(R.id.songs_rec);

        adapter = new UserSongsRecViewAdapter(mainView.getContext());
        adapter.setSongs(songs);

        userSongsRecView.setAdapter(adapter);
        userSongsRecView.setLayoutManager(new LinearLayoutManager(mainView.getContext()));

        updateSongs();
    }

    public void updateSongs() {
        songs.clear();

        String tracks = FavouriteMusic.getFavouriteTitles();
        tracks = tracks.trim();

        if (tracks.length() < 3) return;

        String[] trackList = tracks.split(" ");
        for (String trackName : trackList) {
            if (trackName.equals(" ") || trackName.isEmpty()) continue;

            System.out.printf("Track name: %s\n", trackName);
            songs.add(new Song(trackName, SongsProps.ids.get(SongsProps.songs.indexOf(trackName))));
        }

        adapter.notifyDataSetChanged();
        System.out.println("Updated");
    }
}
