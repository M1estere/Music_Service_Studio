package com.example.music_service.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_service.R;
import com.example.music_service.viewModels.UserPlaylistsViewModel;

public class UserPlaylistsFragment extends Fragment {

    public UserPlaylistsFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static UserPlaylistsFragment newInstance(String param1, String param2) {
        UserPlaylistsFragment fragment = new UserPlaylistsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_playlists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView temp = view.findViewById(R.id.playlists_rec);
        UserPlaylistsViewModel userPlaylistsViewModel = new UserPlaylistsViewModel(view);
    }
}