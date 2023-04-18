package com.example.music_service.models.globals;

import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;

import java.util.ArrayList;

public class PlaylistSystem {

    private static Playlist currentPlaylist;
    public static void setCurrentPlaylist(Playlist currentPlaylist) {
        PlaylistSystem.currentPlaylist = currentPlaylist;
    }
    public static Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public static void fillPlaylistsSection(ArrayList<Playlist> section, int upBorder, int lowBorder, int startPct) {
        int amount = 0;

        if (upBorder - lowBorder > 0) amount = Globs.random.nextInt(upBorder - lowBorder) + lowBorder;
        else amount = lowBorder;

        for (int i = 0; i < amount; i++) {
            section.add(new Playlist("Top " + Globs.random.nextInt(500)));

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

    public static ArrayList<Song> getSongsFromPlaylist(Playlist source) {
        ArrayList<Song> songs = new ArrayList<>();

        ArrayList<String> titles = source.getSongTitles();

        for (String title : titles) {
            songs.add(new Song(title, SongsProps.ids.get(SongsProps.songs.indexOf(title))));
        }

        return songs;
    }

    public static ArrayList<Song> getSongsFromTitles(Playlist source) {
        ArrayList<Song> result = new ArrayList<>();

        ArrayList<String> titles = source.getSongTitles();

        for (String title : titles) {
            result.add(new Song(title, SongsProps.ids.get(SongsProps.songs.indexOf(title))));
        }

        return result;
    }

}
