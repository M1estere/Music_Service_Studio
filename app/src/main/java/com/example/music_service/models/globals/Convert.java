package com.example.music_service.models.globals;

import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;

import java.util.ArrayList;

public class Convert {

    // convert 'break_through_it_all.mp3' to 'Break Through It All'
    public static String getTitleFromPath(String title) {
        int index = title.indexOf('.');
        if (index >= 0)
            title = title.substring(0, index);

        StringBuilder result = new StringBuilder();

        char prevChar = ' ';
        for (int i = 0; i < title.length(); i++) {
            if (Character.isWhitespace(prevChar)) // first iteration
            {
                prevChar = title.charAt(i);
                result.append(Character.toUpperCase(title.charAt(i)));
                continue;
            }
            if (prevChar == '_') {
                result.append(Character.toUpperCase(title.charAt(i)));
                prevChar = title.charAt(i);
                continue;
            }

            if (title.charAt(i) != '_') result.append(title.charAt(i));
            else result.append(' ');

            prevChar = title.charAt(i);
        }

        return result.toString();
    }

    // covert 'Break Through It All' to 'break_through_it_all.mp3'
    public static String getPathFromTitle(String title) {
        String result = title.toLowerCase();
        result = result.replace(' ', '_');

        if (result.contains(".mp3") == false)
            result += ".mp3";

        return result;
    }

    // convert 225000 to 03:45
    public static String GetTimeFromSeconds(long mSeconds) {
        int seconds = (int) (mSeconds / 1000) % 60;
        int minutes = (int) ((mSeconds / (1000 * 60)) % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

    public static Playlist getPlaylistFromSongs(ArrayList<Song> songs, String name) {
        Playlist playlist = new Playlist(name);

        for (Song song : songs)
            playlist.addSong(song.getPath(), song.getId());

        return playlist;
    }

}
