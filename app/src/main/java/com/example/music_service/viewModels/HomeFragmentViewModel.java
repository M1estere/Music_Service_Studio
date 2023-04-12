package com.example.music_service.viewModels;

import android.app.Activity;

import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.MorningRecViewAdapter;
import com.example.music_service.adapters.ProgramPlaylistsRecViewAdapter;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.globals.PlaylistSystem;

import java.util.ArrayList;

public class HomeFragmentViewModel extends BaseObservable {

    private Activity mainActivity;

    private RecyclerView recommendationsRec;
    private RecyclerView popularChoiceRec;

    private RecyclerView morningRec;

    private ArrayList<Playlist> recommendations;
    private ArrayList<Playlist> popular;
    private ArrayList<Playlist> morning;

    public HomeFragmentViewModel(Activity activity) {
        this.mainActivity = activity;

        recommendationsRec = mainActivity.findViewById(R.id.recommendations_rec_view);
        popularChoiceRec = mainActivity.findViewById(R.id.popular_choice_rec_view);

        morningRec = mainActivity.findViewById(R.id.morning_region);

        initRecs();
    }

    private void initRecs() {
        recommendations = new ArrayList<>();
        PlaylistSystem.fillPlaylistsSection(recommendations, 5, 3, 60);

        popular = new ArrayList<>();
        PlaylistSystem.fillPlaylistsSection(popular, 7, 2, 60);

        ProgramPlaylistsRecViewAdapter recommendationsAdapter = new ProgramPlaylistsRecViewAdapter(mainActivity, this);
        recommendationsAdapter.setPlaylists(recommendations);

        recommendationsRec.setAdapter(recommendationsAdapter);
        recommendationsRec.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL,  false));

        ProgramPlaylistsRecViewAdapter popularAdapter = new ProgramPlaylistsRecViewAdapter(mainActivity, this);
        popularAdapter.setPlaylists(popular);

        popularChoiceRec.setAdapter(popularAdapter);
        popularChoiceRec.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));

        morning = new ArrayList<>();
        PlaylistSystem.fillPlaylistsSection(morning, 6, 6, 70);

        MorningRecViewAdapter morningRecViewAdapter = new MorningRecViewAdapter(mainActivity, this);
        morningRecViewAdapter.setPlaylists(morning);

        morningRec.setAdapter(morningRecViewAdapter);
        morningRec.setLayoutManager(new GridLayoutManager(mainActivity, 2));
    }

    public void choosePlaylist(String name) {
        if (name == null) return;

        Playlist temp = findPlaylist(name);

        Player.updateQueue((ArrayList<String>) temp.getSongTitles().clone());
    }

    private Playlist findPlaylist(String name) {
        for (int i = 0; i < recommendations.size(); i++)
            if (recommendations.get(i).getPlaylistName().equals(name))
                return recommendations.get(i);

        for (int i = 0; i < popular.size(); i++)
            if (popular.get(i).getPlaylistName().equals(name))
                return popular.get(i);

        for (int i = 0; i < morning.size(); i++)
            if (morning.get(i).getPlaylistName().equals(name))
                return morning.get(i);

        return new Playlist("Locker");
    }
}
