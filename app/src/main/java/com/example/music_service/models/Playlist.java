package com.example.music_service.models;

import java.util.ArrayList;

public class Playlist {

    private final ArrayList<String> songUris;
    private final String playlistName;
    private ArrayList<String> songTitles;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;

        songTitles = new ArrayList<>();
        songUris = new ArrayList<>();
    }

    public void addSong(String title, String uri) {
        songTitles.add(title);
        songUris.add(uri);
    }

    public void removeSong(int id) {
        songTitles.remove(id);
    }

    public ArrayList<String> getSongTitles() {
        return songTitles;
    }

    public void setSongTitles(ArrayList<String> songTitles) {
        this.songTitles = songTitles;
    }

    public int getSongsAmount() {
        return songTitles.size();
    }

    public String getPlaylistName() {
        return playlistName;
    }
}
