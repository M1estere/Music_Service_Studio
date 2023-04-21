package com.example.music_service.models;

import java.util.ArrayList;

public class Playlist {

    private ArrayList<String> songTitles;
    private ArrayList<String> songUris;

    private String playlistName;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;

        songTitles = new ArrayList<>();
        songUris = new ArrayList<>();
    }

    public void setSongTitles(ArrayList<String> songTitles) {
        this.songTitles = songTitles;
    }

    public void addSong(String title, String uri) {
        songTitles.add(title);
        songUris.add(uri);
    }

    public ArrayList<String> getSongTitles() {
        return songTitles;
    }

    public int getSongsAmount() {
        return songTitles.size();
    }

    public String getPlaylistName() {
        return playlistName;
    }
}
