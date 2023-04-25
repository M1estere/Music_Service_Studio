package com.example.music_service.models;

import androidx.annotation.NonNull;

import com.example.music_service.models.data.SongsProps;

import java.util.ArrayList;

public class Author {

    private String authorName;
    private ArrayList<String> titles;

    public Author(String authorName) {
        this.authorName = authorName;

        titles = new ArrayList<>();

        fillTitles(authorName);
    }

    private void fillTitles(String name) {
        titles = fillTitlesToBlock(name);
    }

    @NonNull
    private ArrayList<String> fillTitlesToBlock(String authorName) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < SongsProps.authors.size(); i++) {
            if (SongsProps.authors.get(i).equals(authorName))
                list.add(SongsProps.songs.get(i));
        }

        return list;
    }

    public String getAuthorName() {
        return authorName;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }
}
