package com.example.music_service;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.QueueRecViewAdapter;
import com.example.music_service.model.globals.Globs;
import com.example.music_service.model.globals.SongsProps;
import com.example.music_service.model.Player;
import com.example.music_service.model.Song;

import java.util.ArrayList;
import java.util.Objects;

public class QueueActivityViewModel extends BaseObservable {

    private Activity mainActivity;

    private ArrayList<Song> songs = new ArrayList<>();
    private RecyclerView queueRecView;

    public QueueActivityViewModel(@NonNull Activity activity) {
        mainActivity = activity;

        queueRecView = activity.findViewById(R.id.queue_rec_view);

        for (String title : Player.getSongs())
            createSong(title, SongsProps.ids.get(SongsProps.songs.indexOf(title)));

        QueueRecViewAdapter adapter = new QueueRecViewAdapter(activity, this);
        adapter.setSongs(songs);

        queueRecView.setAdapter(adapter);
        queueRecView.setLayoutManager(new LinearLayoutManager(activity));
    }

    public void chooseTrack(String title) {
        if (title == null) return;

        int songIndex = findSong(title);

        if (Globs.currentTrackNumber == songIndex) return;

        Globs.currentTrackNumber = songIndex;

        Player.selectTrack(songIndex);
    }

    private int findSong(String title) {
        int songIndex = 0;
        for (int i = 0; i < songs.size(); i++)
            if (Objects.equals(songs.get(i).getTitle(), title)) songIndex = i;

        return songIndex;
    }

    public void backToPlayer() {
        mainActivity.onBackPressed();
    }

    private void createSong(String title, int id) {
        Song songToAdd = new Song(title, id);

        songs.add(songToAdd);
    }

}
