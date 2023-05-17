package com.example.music_service.models;

import com.example.music_service.models.data.PlaylistsData;
import com.example.music_service.models.data.SongsProps;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;

import java.util.ArrayList;

public class Playlist {

    private final String playlistName;
    private ArrayList<String> songTitles;

    private int playlistCoverImage;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;

        songTitles = new ArrayList<>();

        int index = Globs.random.nextInt(PlaylistsData.playlistImages.length);
        playlistCoverImage = PlaylistsData.playlistImages[index];
    }

    public int getPlaylistCoverImage() {
        return playlistCoverImage;
    }

    public void addSong(String title, String uri) {
        songTitles.add(title);
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

    private String findCover(String title) {
        String path = Convert.getPathFromTitle(title);

        int index = SongsProps.songs.indexOf(path);
        return SongsProps.covers.get(index);
    }

    public int getSongsAmount() {
        return songTitles.size();
    }

    public String getPlaylistName() {
        return playlistName;
    }
}
