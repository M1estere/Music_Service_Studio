package com.example.music_service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_service.adapters.ArtistsRecViewAdapter;
import com.example.music_service.model.globals.SongsProps;
import com.example.music_service.model.Author;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LibraryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<Author> authors = new ArrayList<>();

    private RecyclerView authorsRecView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillAuthorsList();
        sortAuthors();

        authorsRecView = view.findViewById(R.id.artists_rec_view);

        ArtistsRecViewAdapter adapter = new ArtistsRecViewAdapter(getActivity());
        adapter.setAuthors(authors);

        authorsRecView.setAdapter(adapter);
        authorsRecView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
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
        ArrayList<String> tempAuthors = new ArrayList<String>();
        for (Author author : authors)
            tempAuthors.add(author.getAuthorName());

        Collections.sort(tempAuthors);
        authors.clear();

        for (String name : tempAuthors)
            authors.add(new Author(name));
    }
}