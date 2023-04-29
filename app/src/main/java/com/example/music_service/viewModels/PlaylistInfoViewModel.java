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
import com.example.music_service.views.BottomSheets;

public class PlaylistInfoViewModel extends BaseObservable {

    private final Activity activity;

    @Bindable
    public String getCurrentPlaylistTitle() {
        return PlaylistSystem.getCurrentPlaylist().getPlaylistName();
    }

    public PlaylistInfoViewModel(Activity act) {
        activity = act;

        RecyclerView tracksRecView = activity.findViewById(R.id.songs_rec_view);

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

    public void openPlaylistInfo() {
        BottomSheets.openPlaylistInfo(activity, PlaylistSystem.getCurrentPlaylist(), false);
    }

    public void back() {
        activity.onBackPressed();
    }
}
