package com.example.music_service.models.globals;

import com.example.music_service.models.Song;

import java.util.ArrayList;
import java.util.Random;

public class Globs {

    public static final String TAG = "Music Service";
    public static Random random = new Random();

    public static ArrayList<Song> currentSongs = new ArrayList<>();
    public static int currentTrackNumber = 0;

    public static boolean recheckLogin = true;

    public static ArrayList<String> getTitles() {
        ArrayList<String> returner = new ArrayList<>();

        for (Song song : currentSongs)
            returner.add(song.getTitle());

        return returner;
    }

    public static ArrayList<String> getTitlesPaths() {
        ArrayList<String> returner = new ArrayList<>();

        for (Song song : currentSongs)
            returner.add(Convert.getPathFromTitle(song.getTitle()));

        return returner;
    }

    public static int getSongNumber(String path) {
        ArrayList<String> titles = getTitles();
        int index = -1;

        for (int i = 0; i < titles.size(); i++) {
            String thisPath = Convert.getPathFromTitle(titles.get(i));

            if (!path.equals(thisPath)) continue;
            index = i;
        }

        return index;
    }

    public static void removeSong(String title) {
        title = Convert.getTitleFromPath(title);

        int index = 0;

        for (int i = 0; i < currentSongs.size(); i++)
            if (currentSongs.get(i).getTitle().equals(title))
                index = i;

        currentSongs.remove(index);
    }

}
