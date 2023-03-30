package com.example.music_service;

import java.util.ArrayList;

public class Playlist {

    private ArrayList<String> songTitles;
    private ArrayList<Integer> songIDs;

    private String playlistName;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;

        songTitles = new ArrayList<>();
        songIDs = new ArrayList<>();
    }

    public ArrayList<String> getSongTitles() {
        return songTitles;
    }

    public void addSong(String title, int id) {
        songTitles.add(title);
        songIDs.add(id);
    }
}
