package com.example.music_service.viewModels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.UserPlaylistsRecViewAdapter;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.globals.PlaylistSystem;

import java.util.ArrayList;

public class UserPlaylistsViewModel extends BaseObservable {

    private final ArrayList<Playlist> playlists = new ArrayList<>();

    public UserPlaylistsViewModel(@NonNull View view) {

        RecyclerView userPlaylistsRecView = view.findViewById(R.id.playlists_rec);

        fillPlaylists();

        UserPlaylistsRecViewAdapter adapter = new UserPlaylistsRecViewAdapter(view.getContext());
        adapter.setPlaylists(playlists);

        userPlaylistsRecView.setAdapter(adapter);
        userPlaylistsRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void fillPlaylists() {
        for (int i = 0; i < 10; i++) {
            playlists.add(new Playlist("Custom #" + (i + 1)));

            PlaylistSystem.fillOnePlaylist(25, playlists.get(i));
        }
    }
}
