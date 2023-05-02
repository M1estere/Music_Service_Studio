package com.example.music_service.viewModels;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.SearchArtistsAdapter;
import com.example.music_service.adapters.SearchPlaylistsAdapter;
import com.example.music_service.adapters.SearchSongsAdapter;
import com.example.music_service.models.Author;
import com.example.music_service.models.CustomPlaylists;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;
import com.example.music_service.models.data.SongsProps;
import com.example.music_service.models.globals.Convert;

import java.util.ArrayList;

public class SearchViewModel extends BaseObservable {

    private final Activity activity;

    private final EditText searchEditText;

    private final RecyclerView songsRecView;
    private final RecyclerView artistsRecView;
    private final RecyclerView playlistsRecView;

    private final TextView placeholder;

    private ArrayList<Song> songs;
    private ArrayList<Author> artists;
    private ArrayList<Playlist> playlists;

    public SearchViewModel(Activity act) {
        activity = act;

        placeholder = activity.findViewById(R.id.placeholder);
        placeholder.setVisibility(View.VISIBLE);

        songsRecView = activity.findViewById(R.id.songs_rec);
        artistsRecView = activity.findViewById(R.id.artists_rec);
        playlistsRecView = activity.findViewById(R.id.playlists_rec);

        searchEditText = activity.findViewById(R.id.search_bar);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString();

                System.out.printf("Current string: %s, length: %d\n", charSequence, charSequence.length());
                if (query.isEmpty() || charSequence.length() < 1) {
                    placeholder.setVisibility(View.VISIBLE);
                    resetRecs();
                } else {
                    placeholder.setVisibility(View.GONE);
                    search(query);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void resetRecs() {
        songs = new ArrayList<>();
        artists = new ArrayList<>();
        playlists = new ArrayList<>();

        setRecViews();
    }

    private void search(String query) {
        query = query.toLowerCase();

        songs = searchSongs(query);
        artists = searchAuthors(query);
        playlists = searchPlaylists(query);

        setRecViews();
    }

    private void setRecViews() {
        SearchSongsAdapter searchSongsAdapter = new SearchSongsAdapter(activity, this);
        searchSongsAdapter.setSongs(songs);

        songsRecView.setAdapter(searchSongsAdapter);
        songsRecView.setLayoutManager(new LinearLayoutManager(activity));

        SearchArtistsAdapter searchArtistsAdapter = new SearchArtistsAdapter(activity);
        searchArtistsAdapter.setArtists(artists);

        artistsRecView.setAdapter(searchArtistsAdapter);
        artistsRecView.setLayoutManager(new LinearLayoutManager(activity));

        SearchPlaylistsAdapter searchPlaylistsAdapter = new SearchPlaylistsAdapter(activity);
        searchPlaylistsAdapter.setPlaylists(playlists);

        playlistsRecView.setAdapter(searchPlaylistsAdapter);
        playlistsRecView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private ArrayList<Song> searchSongs(String query) {
        ArrayList<Song> result = new ArrayList<>();

        // search by song name
        for (String path : SongsProps.songs) {
            if (Convert.getTitleFromPath(path).toLowerCase().contains(query))
                result.add(new Song(path));
        }

        // search by artist name
        for (String name : SongsProps.getDistinctAuthors()) {
            if (name.toLowerCase().contains(query))
                result.add(new Song(SongsProps.songs.get(SongsProps.authors.indexOf(name))));
        }

        return result;
    }

    private ArrayList<Playlist> searchPlaylists(String query) {
        ArrayList<Playlist> result = new ArrayList<>();

        // search by playlist name
        for (String name : CustomPlaylists.getPlaylistNames()) {
            if (name.toLowerCase().contains(query)) {
                Playlist playlist = new Playlist(name);
                playlist.setSongTitles(CustomPlaylists.fillPlaylist(name));

                result.add(playlist);
            }
        }

        return result;
    }

    private ArrayList<Author> searchAuthors(String query) {
        ArrayList<Author> result = new ArrayList<>();

        // search by author name
        for (String author : SongsProps.getDistinctAuthors()) {
            if (author.toLowerCase().contains(query))
                result.add(new Author(author));
        }

        return result;
    }

    public void chooseTrack(String name) {
        int currentTrackIndex = 0;

        for (int i = 0; i < songs.size(); i++) {
            String nameT = Convert.getTitleFromPath(songs.get(i).getTitle());
            if (nameT.equals(name)) {
                currentTrackIndex = i;
                break;
            }
        }

        Player.updateQueue(getTitles(), currentTrackIndex);
    }

    private ArrayList<String> getTitles() {
        ArrayList<String> result = new ArrayList<>();
        for (Song song : songs)
            result.add(song.getPath());

        return result;
    }

    public void back() {
        activity.onBackPressed();
    }
}
