package com.example.music_service.models;

import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.SongsProps;

public class Song {
    private String path;

    private String title;
    private String artist;

    private String uri;
    public String cover;

    public Song(String path, String uri) {
        this.uri = uri;
        this.path = path;

        title = Convert.getTitleFromPath(path);
        int index = SongsProps.songs.indexOf(path);
        artist = SongsProps.authors.get(index);

        cover = SongsProps.covers.get(index);
    }

    public Song(String path) {
        title = path;
        this.path = Convert.getPathFromTitle(path);

        int index = SongsProps.songs.indexOf(path);
        artist = SongsProps.authors.get(index);

        cover = SongsProps.covers.get(index);
    }

    public String getUri() {
        return uri;
    }

    public void setId(String ur) {
        uri = ur;
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

    public String getCover() {
        return cover;
    }
}
