package com.example.music_service.viewModels;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.songs.UserPlaylistTracksAdapter;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.views.BottomSheets;
import com.example.music_service.views.SearchActivity;

import java.util.Objects;

public class UserPlaylistInfoViewModel extends BaseObservable {
    private final Activity activity;

    public UserPlaylistInfoViewModel(Activity act) {
        activity = act;

        RecyclerView tracksRecView = activity.findViewById(R.id.songs_rec_view);

        UserPlaylistTracksAdapter adapter = new UserPlaylistTracksAdapter(activity, this);
        adapter.setSongs(PlaylistSystem.getSongsFromTitles(PlaylistSystem.getCurrentPlaylist()));

        tracksRecView.setAdapter(adapter);
        tracksRecView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Bindable
    public String getCurrentPlaylistTitle() {
        return PlaylistSystem.getCurrentPlaylist().getPlaylistName();
    }

    private int findSong(String title) {
        int songIndex = 0;
        for (int i = 0; i < PlaylistSystem.getCurrentPlaylist().getSongsAmount(); i++)
            if (Objects.equals(PlaylistSystem.getCurrentPlaylist().getSongTitles().get(i), title))
                songIndex = i;

        return songIndex;
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
