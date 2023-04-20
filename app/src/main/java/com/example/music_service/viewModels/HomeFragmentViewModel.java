package com.example.music_service.viewModels;

import android.app.Activity;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.music_service.R;
import com.example.music_service.SwipeToRefresh;
import com.example.music_service.adapters.MorningRecViewAdapter;
import com.example.music_service.adapters.ProgramPlaylistsRecViewAdapter;
import com.example.music_service.models.HomeFragmentData;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.models.globals.SongsProps;

import org.checkerframework.checker.nullness.qual.NonNull;

public class HomeFragmentViewModel extends BaseObservable {

    private View globView;

    private RecyclerView recommendationsRec;
    private RecyclerView popularChoiceRec;
    private RecyclerView morningRec;

    private SwipeToRefresh swipeRefreshLayout;

    private ProgramPlaylistsRecViewAdapter recommendationsAdapter;
    private ProgramPlaylistsRecViewAdapter popularAdapter;
    private MorningRecViewAdapter morningRecViewAdapter;

    public HomeFragmentViewModel(@NonNull View view) {
        globView = view;

        recommendationsRec = globView.findViewById(R.id.recommendations_rec_view);
        popularChoiceRec = globView.findViewById(R.id.popular_choice_rec_view);

        morningRec = globView.findViewById(R.id.morning_region);

        swipeRefreshLayout = globView.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeColors(globView.getContext().getResources().getColor(R.color.red), globView.getContext().getResources().getColor(R.color.purple_200));
        swipeRefreshLayout.offsetTopAndBottom(500);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetPage();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        initRecs();
    }

    public void initRecs() {
        if (HomeFragmentData.recommendations.size() < 1)
            PlaylistSystem.fillPlaylistsSection(HomeFragmentData.recommendations, 5, 3, 60);

        if (HomeFragmentData.popular.size() < 1)
            PlaylistSystem.fillPlaylistsSection(HomeFragmentData.popular, 7, 2, 60);

        recommendationsAdapter = new ProgramPlaylistsRecViewAdapter(globView.getContext(), this);
        recommendationsAdapter.setPlaylists(HomeFragmentData.recommendations);

        recommendationsRec.setAdapter(recommendationsAdapter);
        recommendationsRec.setLayoutManager(new LinearLayoutManager(globView.getContext(), LinearLayoutManager.HORIZONTAL,  false));

        popularAdapter = new ProgramPlaylistsRecViewAdapter(globView.getContext(), this);
        popularAdapter.setPlaylists(HomeFragmentData.popular);

        popularChoiceRec.setAdapter(popularAdapter);
        popularChoiceRec.setLayoutManager(new LinearLayoutManager(globView.getContext(), LinearLayoutManager.HORIZONTAL, false));

        if (HomeFragmentData.morningPlaylists.size() < 1)
            PlaylistSystem.fillPlaylistsSection(HomeFragmentData.morningPlaylists, 6, 6, 70);

        morningRecViewAdapter = new MorningRecViewAdapter(globView.getContext(), this);
        morningRecViewAdapter.setPlaylists(HomeFragmentData.morningPlaylists);

        morningRec.setAdapter(morningRecViewAdapter);
        morningRec.setLayoutManager(new GridLayoutManager(globView.getContext(), 2));
    }

    private void resetPage() {
        HomeFragmentData.recommendations.clear();
        PlaylistSystem.fillPlaylistsSection(HomeFragmentData.recommendations, 5, 3, 60);

        recommendationsAdapter.setPlaylists(HomeFragmentData.recommendations);

        HomeFragmentData.popular.clear();
        PlaylistSystem.fillPlaylistsSection(HomeFragmentData.popular, 7, 2, 60);
        popularAdapter.setPlaylists(HomeFragmentData.popular);

        HomeFragmentData.morningPlaylists.clear();
        PlaylistSystem.fillPlaylistsSection(HomeFragmentData.morningPlaylists, 6, 6, 70);
        morningRecViewAdapter.setPlaylists(HomeFragmentData.morningPlaylists);
    }

}
