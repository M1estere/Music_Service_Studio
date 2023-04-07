package com.example.music_service.model.globals;

import com.example.music_service.model.Song;

import java.util.ArrayList;

public class SongsProps {

    public static ArrayList<String> songs = new ArrayList<>();
    public static ArrayList<String> authors = new ArrayList<>();
    public static ArrayList<Integer> ids = new ArrayList<>();
    public static ArrayList<String> covers = new ArrayList<>();

    public static Song getSongByName(String songName) {
        Song returner = new Song(songName);

        return returner;
    }

}
