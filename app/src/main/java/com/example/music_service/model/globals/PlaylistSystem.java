package com.example.music_service.model.globals;

import com.example.music_service.model.Playlist;

import java.util.ArrayList;

public class PlaylistSystem {

    public static void fillPlaylistsSection(ArrayList<Playlist> section, int upBorder, int lowBorder, int startPct) {
        int amount = 0;

        if (upBorder - lowBorder > 0) amount = Globs.random.nextInt(upBorder - lowBorder) + lowBorder;
        else amount = lowBorder;

        for (int i = 0; i < amount; i++) {
            section.add(new Playlist("Top 2023" + Globs.random.nextInt(500)));

            fillOnePlaylist(startPct, section.get(i));
        }
    }

    public static void fillOnePlaylist(float startSongsPercentage, Playlist listToFill) {
        startSongsPercentage /= 100;

        ArrayList<String> titles = new ArrayList<>();
        titles = (ArrayList<String>) SongsProps.songs.clone();

        int startSongsAmount = (int) (titles.size() * startSongsPercentage);

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
