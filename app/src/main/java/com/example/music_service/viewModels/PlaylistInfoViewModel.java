package com.example.music_service.viewModels;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.PlaylistTracksAdapter;
import com.example.music_service.models.CustomPlaylists;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.views.BottomSheets;
import com.example.music_service.views.SearchActivity;

public class PlaylistInfoViewModel extends BaseObservable {

    private final Activity activity;

    public PlaylistInfoViewModel(Activity act) {
        activity = act;

        RecyclerView tracksRecView = activity.findViewById(R.id.songs_rec_view);

        PlaylistTracksAdapter adapter = new PlaylistTracksAdapter(activity);
        adapter.setSongs(PlaylistSystem.getSongsFromTitles(PlaylistSystem.getCurrentPlaylist()));

        tracksRecView.setAdapter(adapter);
        tracksRecView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Bindable
    public String getCurrentPlaylistTitle() {
        return PlaylistSystem.getCurrentPlaylist().getPlaylistName();
    }

    public void savePlaylist() {
        CustomPlaylists.savingPlaylist(activity, PlaylistSystem.getCurrentPlaylist().getSongTitles());
    }

    public void startPlaylist() {
        Playlist current = PlaylistSystem.getCurrentPlaylist();
        Player.updateQueue(current.getSongTitles(), 0);

        activity.onBackPressed();
    }

    public void openSearch() {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    public void openPlaylistInfo() {
        BottomSheets.openPlaylistInfo(activity, PlaylistSystem.getCurrentPlaylist(), false);
    }

    public void back() {
        activity.onBackPressed();
    }
}
