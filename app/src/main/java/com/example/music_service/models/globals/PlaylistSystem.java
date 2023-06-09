package com.example.music_service.models.globals;

import com.example.music_service.models.Author;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;
import com.example.music_service.models.data.PlaylistsData;
import com.example.music_service.models.data.SongsProps;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaylistSystem {

    public static ArrayList<Integer> localCounts = new ArrayList<>();
    private static Author currentAuthor;
    private static Playlist currentPlaylist;

    public static Author getCurrentAuthor() {
        return currentAuthor;
    }

    public static void setCurrentAuthor(Author currentAuthor) {
        PlaylistSystem.currentAuthor = currentAuthor;
    }

    public static Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public static void setCurrentPlaylist(Playlist currentPlaylist) {
        PlaylistSystem.currentPlaylist = currentPlaylist;
    }

    public static void removeFromCurrent(int id) {
        currentPlaylist.removeSong(id);
    }

    public static ArrayList<String> getTitlesFromSongs(ArrayList<Song> songs) {
        ArrayList<String> result = new ArrayList<>();

        for (Song song : songs) {
            result.add(song.getPath());
        }

        return result;
    }

    public static void load() {
        localCounts.clear();
        for (int i = 0; i < PlaylistsData.playlistNames.length; i++) localCounts.add(0);
    }

    public static void fillPlaylistsSection(ArrayList<Playlist> section, int upBorder, int lowBorder, int startPct) {
        int amount = 0;

        ArrayList<String> localNames = new ArrayList<>(Arrays.asList(PlaylistsData.playlistNames));

        if (upBorder - lowBorder > 0)
            amount = Globs.random.nextInt(upBorder - lowBorder) + lowBorder;
        else amount = lowBorder;

        for (int i = 0; i < amount; i++) {
            int index = Globs.random.nextInt(localNames.size());
            String name = String.format("%s %d", localNames.get(index), localCounts.get(index) + 1);
            localCounts.set(index, localCounts.get(index) + 1);

            section.add(new Playlist(name));

            fillOnePlaylist(startPct, section.get(i));
        }
    }

    public static void fillOnePlaylist(float startSongsPercentage, Playlist listToFill) {
        startSongsPercentage /= 100;

        ArrayList<String> titles = new ArrayList<>();
        titles = (ArrayList<String>) SongsProps.songs.clone();

//        titles.add("moves_like_jagger.mp3");
//        titles.add("moves_like_jagger.mp3");
//        titles.add("moves_like_jagger.mp3");
//        titles.add("moves_like_jagger.mp3");

        int startSongsAmount = (int) (titles.size() * startSongsPercentage);

        int length = Globs.random.nextInt(titles.size() - startSongsAmount) + startSongsAmount;

        for (int i = 0; i < length; i++) {
            int randomIndex = Globs.random.nextInt(titles.size());
            String songTitle = titles.get(randomIndex);
            //String songUri = SongsProps.uris.get(randomIndex);

            titles.remove(songTitle);
//            listToFill.addSong(songTitle, songUri);
            listToFill.addSong(songTitle);
        }
    }

    public static ArrayList<Song> getSongsFromPlaylist(Playlist source) {
        ArrayList<Song> songs = new ArrayList<>();

        ArrayList<String> titles = source.getSongTitles();

        for (String title : titles) {
            songs.add(new Song(title, SongsProps.uris.get(SongsProps.songs.indexOf(title))));
        }

        return songs;
    }

    public static ArrayList<Song> getSongsFromTitles(Playlist source) {
        ArrayList<Song> result = new ArrayList<>();

        ArrayList<String> titles = source.getSongTitles();

        for (String title : titles)
            result.add(new Song(title, SongsProps.uris.get(SongsProps.songs.indexOf(title))));

        return result;
    }

    public static ArrayList<Song> getSongsFromTitles(ArrayList<String> source) {
        ArrayList<Song> result = new ArrayList<>();

        for (String title : source)
            result.add(new Song(title, SongsProps.uris.get(SongsProps.songs.indexOf(title))));

        return result;
    }

    public static Playlist getPlaylistOfSongs(String name, ArrayList<String> songs) {
        Playlist playlist = new Playlist(name);
        playlist.setSongTitles(songs);

        return playlist;
    }

    public static Playlist getPlaylistOfSongsSongs(String name, ArrayList<Song> songs) {
        Playlist playlist = new Playlist(name);

        ArrayList<String> titles = new ArrayList<>();
        for (Song song : songs) titles.add(song.getPath());

        playlist.setSongTitles(titles);
        return playlist;
    }

    public static String findArtistFirstSong(String name) {
        return SongsProps.covers.get(SongsProps.authors.indexOf(name));
    }

}
