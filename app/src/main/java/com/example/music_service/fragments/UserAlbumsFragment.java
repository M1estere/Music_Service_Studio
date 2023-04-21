package com.example.music_service.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_service.R;

public class UserAlbumsFragment extends Fragment {

    public UserAlbumsFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static UserAlbumsFragment newInstance(String param1, String param2) {
        UserAlbumsFragment fragment = new UserAlbumsFragment();
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
        return inflater.inflate(R.layout.fragment_user_albums, container, false);
    }
}