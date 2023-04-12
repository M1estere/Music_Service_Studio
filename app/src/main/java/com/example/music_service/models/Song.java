package com.example.music_service.models;

import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.SongsProps;

public class Song {
    private String path;

    private String title;
    private String artist;

    private int id;
    // public String cover;
    private int duration;
    private String durationString;

    public Song(String path, int id) {
        this.id = id;
        this.path = path;

        title = Convert.getTitleFromPath(path);
        artist = SongsProps.authors.get(SongsProps.songs.indexOf(path));

        //getDuration();
    }

    public Song(String path) {
        title = path;
        this.path = Convert.getPathFromTitle(path);

        artist = SongsProps.authors.get(SongsProps.songs.indexOf(this.path));

        //getDuration();
    }

    /*private void getDuration() {
        String mediaPath = Uri.parse("android.resource://com.example.music_service/raw/get_lucky.mp3").getPath();
        System.out.printf("String: %s\n", mediaPath);

        // load data file
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(mediaPath);

        String out = "";

        // convert duration to minute:seconds
        String duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        long dur = Long.parseLong(duration);
        String seconds = String.valueOf((dur % 60000) / 1000);

        String minutes = String.valueOf(dur / 60000);
        out = minutes + ":" + seconds;
        if (seconds.length() == 1) {
            durationString = ("0" + minutes + ":0" + seconds);
        } else {
            durationString = ("0" + minutes + ":" + seconds);
        }

        // close object
        try {
            metaRetriever.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

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

    public String getDurationString() {
        return durationString;
    }
}
