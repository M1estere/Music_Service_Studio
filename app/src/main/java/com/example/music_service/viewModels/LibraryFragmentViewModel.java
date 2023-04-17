package com.example.music_service.viewModels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.ArtistsRecViewAdapter;
import com.example.music_service.adapters.BestTracksAdapter;
import com.example.music_service.adapters.DailyTopRecViewAdapter;
import com.example.music_service.models.Author;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.models.globals.SongsProps;

import java.util.ArrayList;
import java.util.Collections;

public class LibraryFragmentViewModel extends BaseObservable {

    private ArrayList<Author> authors;
    private ArrayList<Song> titles;

    private RecyclerView authorsRecView;
    private RecyclerView dailyTopRecView;
    private RecyclerView bestRecView;

    private Playlist topPlaylist;
    private ArrayList<Song> bestSongs;

    public LibraryFragmentViewModel(@NonNull View view) {
        authors = new ArrayList<>();
        titles = new ArrayList<>();
        bestSongs = new ArrayList<>();

        twoBest();

        fillAuthorsList();
        sortAuthors();

        topPlaylist = new Playlist("Daily Top");
        PlaylistSystem.fillOnePlaylist(10, topPlaylist);
        titles = PlaylistSystem.getSongsFromPlaylist(topPlaylist);

        authorsRecView = view.findViewById(R.id.artists_rec_view);

        ArtistsRecViewAdapter artistsAdapter = new ArtistsRecViewAdapter(view.getContext());
        artistsAdapter.setAuthors(authors);

        authorsRecView.setAdapter(artistsAdapter);
        authorsRecView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));


        dailyTopRecView = view.findViewById(R.id.daily_top_rec_view);

        DailyTopRecViewAdapter dailyTopAdapter = new DailyTopRecViewAdapter(view.getContext(), this);
        dailyTopAdapter.setSongs(titles);

        dailyTopRecView.setAdapter(dailyTopAdapter);
        dailyTopRecView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));


        bestRecView = view.findViewById(R.id.best_rec_view);

        BestTracksAdapter bestTracksAdapter = new BestTracksAdapter(view.getContext(), this);
        bestTracksAdapter.setSongs(bestSongs);

        bestRecView.setAdapter(bestTracksAdapter);
        bestRecView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        bestRecView.suppressLayout(true);
    }

    private void twoBest() {
        ArrayList<String> titles = (ArrayList<String>) SongsProps.songs.clone();
        Collections.shuffle(titles);

        for (int i = 0; i < 2; i++) {
            int randomIndex = Globs.random.nextInt(titles.size());
            String songTitle = titles.get(randomIndex);

            int songId = SongsProps.ids.get(randomIndex);

            titles.remove(songTitle);
            bestSongs.add(new Song(songTitle, SongsProps.ids.get(SongsProps.songs.indexOf(songTitle))));
        }
    }

    private void fillAuthorsList() {
        ArrayList<String> checked = new ArrayList<>();

        for (String author : SongsProps.authors) {
            if (checked.contains(author)) continue;

            checked.add(author);

            authors.add(new Author(author));
        }
    }

    private void sortAuthors() {
        ArrayList<String> tempAuthors = new ArrayList<>();
        for (Author author : authors)
            tempAuthors.add(author.getAuthorName());

        Collections.sort(tempAuthors);
        authors.clear();

        for (String name : tempAuthors)
            authors.add(new Author(name));
    }

    public void chooseTrack(String name) {
        int currentTrackIndex = 0;
        for (int i = 0; i < topPlaylist.getSongsAmount(); i++)
        {
            String nameT = Convert.getTitleFromPath(topPlaylist.getSongTitles().get(i));
            if (nameT.equals(name)) currentTrackIndex = i;
        }

        Player.updateQueue(topPlaylist.getSongTitles(), currentTrackIndex);
    }

    public void chooseFromBest(String name) {
        int currentTrackIndex = 0;
        for (int i = 0; i < bestSongs.size(); i++)
        {
            String nameT = bestSongs.get(i).getTitle();
            if (nameT.equals(name)) currentTrackIndex = i;
        }

        Player.updateQueue(Convert.getPlaylistFromSongs(bestSongs, "Best Songs").getSongTitles(), currentTrackIndex);
    }

}
