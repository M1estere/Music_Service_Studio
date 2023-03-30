package com.example.music_service;

import java.util.ArrayList;

public class PlaylistSystem {

    public static void fillOnePlaylist(float startSongsPercentage, Playlist listToFill) {
        startSongsPercentage /= 100;

        ArrayList<String> titles = new ArrayList<>();
        titles = (ArrayList<String>) SongsProps.songs.clone();

        int startSongsAmount = (int) ((titles.size() / 100) * startSongsPercentage);

        int length = Globs.random.nextInt(titles.size() - startSongsAmount) + startSongsAmount;

        for (int i = 0; i < length; i++) {
            int randomIndex = Globs.random.nextInt(titles.size());
            String songTitle = titles.get(randomIndex);
            int songId = SongsProps.ids.get(randomIndex);

            titles.remove(songTitle);
            listToFill.addSong(songTitle, songId);
        }
    }

}
