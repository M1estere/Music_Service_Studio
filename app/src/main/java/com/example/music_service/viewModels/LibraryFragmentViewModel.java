package com.example.music_service.viewModels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.music_service.R;
import com.example.music_service.models.SwipeToRefresh;
import com.example.music_service.adapters.ArtistsRecViewAdapter;
import com.example.music_service.adapters.BestTracksAdapter;
import com.example.music_service.adapters.DailyTopRecViewAdapter;
import com.example.music_service.models.Author;
import com.example.music_service.models.data.LibraryFragmentData;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.models.data.SongsProps;

import java.util.ArrayList;
import java.util.Collections;

public class LibraryFragmentViewModel extends BaseObservable {

    private final ArrayList<Author> authors;

    private final RecyclerView bestRecView;
    private final SwipeToRefresh swipeToRefresh;

    private final DailyTopRecViewAdapter dailyTopAdapter;
    private final BestTracksAdapter bestTracksAdapter;

    public LibraryFragmentViewModel(@NonNull View view) {
        swipeToRefresh = view.findViewById(R.id.swipe);
        swipeToRefresh.setColorSchemeColors(view.getContext().getResources().getColor(R.color.red), view.getContext().getResources().getColor(R.color.purple_200));
        swipeToRefresh.offsetTopAndBottom(500);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetPage();
                swipeToRefresh.setRefreshing(false);
            }
        });

        authors = new ArrayList<>();

        twoBest();
        fillAuthorsList();
        sortAuthors();

        if (LibraryFragmentData.dailyTopSongs == null) {
            LibraryFragmentData.dailyTopSongs = new Playlist("Daily Top");
            PlaylistSystem.fillOnePlaylist(10, LibraryFragmentData.dailyTopSongs);

            LibraryFragmentData.dailyTops = PlaylistSystem.getSongsFromPlaylist(LibraryFragmentData.dailyTopSongs);
        }

        RecyclerView authorsRecView = view.findViewById(R.id.artists_rec_view);

        ArtistsRecViewAdapter artistsAdapter = new ArtistsRecViewAdapter(view.getContext());
        artistsAdapter.setAuthors(authors);

        authorsRecView.setAdapter(artistsAdapter);
        authorsRecView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));


        RecyclerView dailyTopRecView = view.findViewById(R.id.daily_top_rec_view);

        dailyTopAdapter = new DailyTopRecViewAdapter(view.getContext(), this);
        dailyTopAdapter.setSongs(LibraryFragmentData.dailyTops);

        dailyTopRecView.setAdapter(dailyTopAdapter);
        dailyTopRecView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));


        bestRecView = view.findViewById(R.id.best_rec_view);

        bestTracksAdapter = new BestTracksAdapter(view.getContext(), this);
        bestTracksAdapter.setSongs(LibraryFragmentData.bestSongs);

        bestRecView.setAdapter(bestTracksAdapter);
        bestRecView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        bestRecView.suppressLayout(true);
    }

    private void resetPage() {
        bestRecView.suppressLayout(false);
        LibraryFragmentData.dailyTopSongs = null;
        LibraryFragmentData.dailyTops.clear();

        LibraryFragmentData.bestSongs.clear();
        twoBest();

        LibraryFragmentData.dailyTopSongs = new Playlist("Daily Top");
        PlaylistSystem.fillOnePlaylist(10, LibraryFragmentData.dailyTopSongs);
        LibraryFragmentData.dailyTops = PlaylistSystem.getSongsFromPlaylist(LibraryFragmentData.dailyTopSongs);

        dailyTopAdapter.setSongs(LibraryFragmentData.dailyTops);

        bestTracksAdapter.setSongs(LibraryFragmentData.bestSongs);
        bestRecView.suppressLayout(true);
    }

    private void twoBest() {
        if (LibraryFragmentData.bestSongs.size() >= 2) return;

        ArrayList<String> titles = (ArrayList<String>) SongsProps.songs.clone();
        Collections.shuffle(titles);

        for (int i = 0; i < 2; i++) {
            int randomIndex = Globs.random.nextInt(titles.size());
            String songTitle = titles.get(randomIndex);

            String songUri = SongsProps.uris.get(randomIndex);

            titles.remove(songTitle);
            LibraryFragmentData.bestSongs.add(new Song(songTitle, SongsProps.uris.get(SongsProps.songs.indexOf(songTitle))));
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

        for (int i = 0; i < LibraryFragmentData.dailyTopSongs.getSongsAmount(); i++) {
            String nameT = Convert.getTitleFromPath(LibraryFragmentData.dailyTopSongs.getSongTitles().get(i));
            if (nameT.equals(name)) {
                currentTrackIndex = i;
                break;
            }
        }

        Player.updateQueue(LibraryFragmentData.dailyTopSongs.getSongTitles(), currentTrackIndex);
    }

    public void chooseFromBest(String name) {
        int currentTrackIndex = 0;
        for (int i = 0; i < LibraryFragmentData.bestSongs.size(); i++)
        {
            String nameT = LibraryFragmentData.bestSongs.get(i).getTitle();
            if (nameT.equals(name)) currentTrackIndex = i;
        }

        Player.updateQueue(Convert.getPlaylistFromSongs(LibraryFragmentData.bestSongs, "Best Songs").getSongTitles(), currentTrackIndex);
    }

}
