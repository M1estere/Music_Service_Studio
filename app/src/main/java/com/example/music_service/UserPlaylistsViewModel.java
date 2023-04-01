package com.example.music_service;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.adapters.UserPlaylistsRecViewAdapter;
import com.example.music_service.model.Playlist;
import com.example.music_service.model.globals.PlaylistSystem;

import java.util.ArrayList;

public class UserPlaylistsViewModel extends BaseObservable {

    private View mainView;

    private ArrayList<Playlist> playlists = new ArrayList<>();
    private RecyclerView userPlaylistsRecView;

    public UserPlaylistsViewModel(@NonNull View view) {
        mainView = view;

        userPlaylistsRecView = view.findViewById(R.id.playlists_rec);

        fillPlaylists();

        UserPlaylistsRecViewAdapter adapter = new UserPlaylistsRecViewAdapter(mainView.getContext());
        adapter.setPlaylists(playlists);

        userPlaylistsRecView.setAdapter(adapter);
        userPlaylistsRecView.setLayoutManager(new LinearLayoutManager(mainView.getContext()));
    }

    private void fillPlaylists() {
        for (int i = 0; i < 10; i++) {
            playlists.add(new Playlist("Custom #" + (i + 1)));

            PlaylistSystem.fillOnePlaylist(25, playlists.get(i));
        }
    }
}
