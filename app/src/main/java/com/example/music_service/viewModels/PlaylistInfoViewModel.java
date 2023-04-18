package com.example.music_service.viewModels;

import android.app.Activity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.PlaylistTracksAdapter;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.PlaylistSystem;

public class PlaylistInfoViewModel extends BaseObservable {

    private Activity activity;

    private RecyclerView tracksRecView;

    @Bindable
    public String getCurrentPlaylistTitle() {
        return PlaylistSystem.getCurrentPlaylist().getPlaylistName();
    }

    public PlaylistInfoViewModel(Activity act) {
        activity = act;

        tracksRecView = activity.findViewById(R.id.songs_rec_view);

        PlaylistTracksAdapter adapter = new PlaylistTracksAdapter(activity, this);
        adapter.setSongs(PlaylistSystem.getSongsFromTitles(PlaylistSystem.getCurrentPlaylist()));

        tracksRecView.setAdapter(adapter);
        tracksRecView.setLayoutManager(new LinearLayoutManager(activity));
    }

    public void startPlaylist() {
        Playlist current = PlaylistSystem.getCurrentPlaylist();
        Player.updateQueue(current.getSongTitles(), 0);

        activity.onBackPressed();
    }

    public void chooseTrack(String name) {
        Playlist current = PlaylistSystem.getCurrentPlaylist();
        int currentTrackIndex = 0;
        for (int i = 0; i < current.getSongsAmount(); i++) {
            String nameT = Convert.getTitleFromPath(current.getSongTitles().get(i));
            if (nameT.equals(name)) currentTrackIndex = i;
        }

        Player.updateQueue(current.getSongTitles(), currentTrackIndex);
    }

    public void back() {
        activity.onBackPressed();
    }
}