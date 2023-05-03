package com.example.music_service.viewModels;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_service.R;
import com.example.music_service.adapters.songs.ArtistTracksAdapter;
import com.example.music_service.models.Author;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.views.SearchActivity;

public class ArtistInfoViewModel extends BaseObservable {

    private final Activity activity;

    public ArtistInfoViewModel(Activity act) {
        activity = act;

        setName();
        setImage();

        RecyclerView tracksRecView = activity.findViewById(R.id.songs_rec_view);

        ArtistTracksAdapter adapter = new ArtistTracksAdapter(activity);
        adapter.setSongs(PlaylistSystem.getSongsFromTitles(PlaylistSystem.getCurrentAuthor().getTitles()));

        tracksRecView.setAdapter(adapter);
        tracksRecView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void setName() {
        TextView artistName = activity.findViewById(R.id.artist_name);
        artistName.setSelected(true);
    }

    private void setImage() {
        ImageView image = activity.findViewById(R.id.image);

        Glide.with(activity)
                .load(PlaylistSystem.findArtistFirstSong(PlaylistSystem.getCurrentAuthor().getAuthorName()))
                .thumbnail(0.05f)
                .into(image);
    }

    @Bindable
    public String getCurrentArtistName() {
        return PlaylistSystem.getCurrentAuthor().getAuthorName();
    }

    public void startPlaylist() {
        Author currentAuthor = PlaylistSystem.getCurrentAuthor();

        Playlist current = PlaylistSystem.getPlaylistOfSongs(currentAuthor.getAuthorName(), currentAuthor.getTitles());
        Player.updateQueue(current.getSongTitles(), 0);

        activity.onBackPressed();
    }

    public void openSearch() {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    public void back() {
        activity.onBackPressed();
    }

}
