package com.example.music_service.viewModels;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_service.R;
import com.example.music_service.adapters.ArtistTracksAdapter;
import com.example.music_service.models.Author;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.PlaylistSystem;

public class ArtistInfoViewModel extends BaseObservable {

    private Activity activity;

    @Bindable
    public String getCurrentArtistName() {
        return PlaylistSystem.getCurrentAuthor().getAuthorName();
    }

    public ArtistInfoViewModel(Activity act) {
        activity = act;

        TextView artistName = activity.findViewById(R.id.artist_name);
        artistName.setSelected(true);

        ImageView image = activity.findViewById(R.id.image);

        Glide.with(activity)
                .load(PlaylistSystem.findArtistFirstSong(PlaylistSystem.getCurrentAuthor().getAuthorName()))
                .thumbnail(0.05f)
                .into(image);

        RecyclerView tracksRecView = activity.findViewById(R.id.songs_rec_view);

        ArtistTracksAdapter adapter = new ArtistTracksAdapter(activity, this);
        adapter.setSongs(PlaylistSystem.getSongsFromTitles(PlaylistSystem.getCurrentAuthor().getTitles()));

        tracksRecView.setAdapter(adapter);
        tracksRecView.setLayoutManager(new LinearLayoutManager(activity));
    }

    public void startPlaylist() {
        Author currentAuthor = PlaylistSystem.getCurrentAuthor();

        Playlist current = PlaylistSystem.getPlaylistOfSongs(currentAuthor.getAuthorName(), currentAuthor.getTitles());
        Player.updateQueue(current.getSongTitles(), 0);

        activity.onBackPressed();
    }

    public void chooseTrack(String name) {
        System.out.printf("Current clicked name: %s\n", name);
        Author currentAuthor = PlaylistSystem.getCurrentAuthor();

        Playlist current = PlaylistSystem.getPlaylistOfSongs(currentAuthor.getAuthorName(), currentAuthor.getTitles());
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
