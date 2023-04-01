package com.example.music_service.model;

import com.example.music_service.model.globals.Convert;
import com.example.music_service.model.globals.SongsProps;

public class Song {
    private String path;

    private String title;
    private String artist;

    private int id;
    // public String cover;

    public Song(String path, int id) {
        this.id = id;
        this.path = path;

        title = Convert.getTitleFromPath(path);
        artist = SongsProps.authors.get(SongsProps.songs.indexOf(path));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
