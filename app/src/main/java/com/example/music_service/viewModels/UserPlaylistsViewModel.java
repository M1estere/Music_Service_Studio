package com.example.music_service.viewModels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.UserPlaylistsRecViewAdapter;
import com.example.music_service.models.CustomPlaylists;
import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;
import com.example.music_service.models.data.SongsProps;
import com.example.music_service.models.globals.PlaylistSystem;

import java.util.ArrayList;

public class UserPlaylistsViewModel extends BaseObservable {

    private ArrayList<Playlist> playlists = new ArrayList<>();

    private UserPlaylistsRecViewAdapter adapter;

    public UserPlaylistsViewModel(@NonNull View view) {
        CustomPlaylists.setUserPlaylistsViewModel(this);

        RecyclerView userPlaylistsRecView = view.findViewById(R.id.playlists_rec);

        adapter = new UserPlaylistsRecViewAdapter(view.getContext());
        adapter.setPlaylists(playlists);

        userPlaylistsRecView.setAdapter(adapter);
        userPlaylistsRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        updatePlaylists();
    }

    public void updatePlaylists() {
        playlists.clear();

        ArrayList<String> names = CustomPlaylists.getPlaylistNames();
        ArrayList<String> contents = CustomPlaylists.getPlaylistContents();

        System.out.printf("Names total amount: %d\n", names.size());

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            ArrayList<String> content = CustomPlaylists.fromStringsToPlaylist(contents.get(i));

            Playlist playlist = new Playlist(name);
            playlist.setSongTitles(content);
            playlists.add(playlist);
        }

        adapter.notifyDataSetChanged();
    }
}
