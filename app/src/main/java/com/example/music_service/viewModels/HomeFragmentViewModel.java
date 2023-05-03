package com.example.music_service.viewModels;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.music_service.R;
import com.example.music_service.adapters.playlists.MorningRecViewAdapter;
import com.example.music_service.adapters.playlists.ProgramPlaylistsRecViewAdapter;
import com.example.music_service.models.SwipeToRefresh;
import com.example.music_service.models.data.HomeFragmentData;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.views.SearchActivity;

import org.checkerframework.checker.nullness.qual.NonNull;

public class HomeFragmentViewModel extends BaseObservable {

    private final View globView;

    private final RecyclerView recommendationsRec;
    private final RecyclerView popularChoiceRec;
    private final RecyclerView morningRec;

    private final SwipeToRefresh swipeRefreshLayout;

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

        ImageButton searchButton = globView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearch(globView.getContext());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetPage();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        initRecs();
    }

    private void openSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public void initRecs() {
        if (HomeFragmentData.recommendations.size() < 1)
            PlaylistSystem.fillPlaylistsSection(HomeFragmentData.recommendations, 5, 3, 60);

        if (HomeFragmentData.popular.size() < 1)
            PlaylistSystem.fillPlaylistsSection(HomeFragmentData.popular, 7, 2, 60);

        recommendationsAdapter = new ProgramPlaylistsRecViewAdapter(globView.getContext());
        recommendationsAdapter.setPlaylists(HomeFragmentData.recommendations);

        recommendationsRec.setAdapter(recommendationsAdapter);
        recommendationsRec.setLayoutManager(new LinearLayoutManager(globView.getContext(), LinearLayoutManager.HORIZONTAL, false));

        popularAdapter = new ProgramPlaylistsRecViewAdapter(globView.getContext());
        popularAdapter.setPlaylists(HomeFragmentData.popular);

        popularChoiceRec.setAdapter(popularAdapter);
        popularChoiceRec.setLayoutManager(new LinearLayoutManager(globView.getContext(), LinearLayoutManager.HORIZONTAL, false));

        if (HomeFragmentData.morningPlaylists.size() < 1)
            PlaylistSystem.fillPlaylistsSection(HomeFragmentData.morningPlaylists, 6, 6, 70);

        morningRecViewAdapter = new MorningRecViewAdapter(globView.getContext());
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
