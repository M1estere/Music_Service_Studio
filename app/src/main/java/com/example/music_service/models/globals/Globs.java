package com.example.music_service.models.globals;

import com.example.music_service.R;
import com.example.music_service.models.Song;

import java.util.ArrayList;
import java.util.Random;

public class Globs {
    public static boolean recheckLogin = true;

    public static final String TAG = "Music Service";

    public static Random random = new Random();

    public static ArrayList<Song> currentSongs = new ArrayList<>();
    public static ArrayList<String> getTitles() {
        ArrayList<String> returner = new ArrayList<>();

        for (Song song : currentSongs)
            returner.add(song.getTitle());

        return returner;
    }

    public static int getSongNumber(String path) {
        ArrayList<String> titles = getTitles();
        int index = -1;

        for (int i = 0; i < titles.size(); i++) {
            String thisPath = Convert.getPathFromTitle(titles.get(i));

            if (path.equals(thisPath) == false) continue;
            index = i;
        }

        return index;
    }

    public static int currentTrackNumber = 0;

    public static void fillAllSongs() {
        if (SongsProps.songs.size() > 0) return;

        addSong(R.raw.alive, "alive.mp3", "Warbly Jets", "warbly.png");
        addSong(R.raw.ancients, "ancients.mp3", "Nier OST", "nier_replicant.png");
        addSong(R.raw.animals, "animals.mp3", "Maroon 5", "maroon_v.png");
        addSong(R.raw.ashes_of_dreams, "ashes_of_dreams.mp3", "Nier OST", "nier_replicant.png");
        addSong(R.raw.bad, "bad.mp3", "Michael Jackson", "michael_jackson.jpg");

        addSong(R.raw.beat_it, "beat_it.mp3", "Michael Jackson", "michael_jackson.jpg");
        addSong(R.raw.believer, "believer.mp3", "Imagine Dragons", "imagine_dragons.jpg");
        addSong(R.raw.billie_jean, "billie_jean.mp3", "Michael Jackson", "michael_jackson.jpg");
        addSong(R.raw.break_through_it_all, "break_through_it_all.mp3", "Sonic OST", "sonic_frontiers_ost.png");
        addSong(R.raw.circles, "circles.mp3", "Post Malone", "post_malone.png");

        addSong(R.raw.cold_heart, "cold_heart.mp3", "Elton John", "elton.png");
        addSong(R.raw.easier, "easier.mp3", "5SOS", "seconds_of_summer.png");
        addSong(R.raw.electrical_storm, "electrical_storm.mp3", "U2", "u2.png");
        addSong(R.raw.endless_possibility, "endless_possibility.mp3", "Sonic OST", "sonic_unleashed_ost.jpg");

        addSong(R.raw.get_lucky, "get_lucky.mp3", "Daft Punk", "daft_punk.png");
        addSong(R.raw.grandmother, "grandmother.mp3", "Nier OST", "nier_replicant.png");
        addSong(R.raw.his_world, "his_world.mp3", "Sonic OST", "sonic06_ost.png");
        addSong(R.raw.i_feel_it_coming, "i_feel_it_coming.mp3", "The Weeknd", "weeknd.png");

        addSong(R.raw.in_your_eyes, "in_your_eyes.mp3", "The Weeknd", "weeknd.png");
        addSong(R.raw.infinite, "infinite.mp3", "Sonic OST", "sonic_forces_ost.png");
        addSong(R.raw.last_christmas, "last_christmas.mp3", "WHAM!", "last_christmas.jpg");
        addSong(R.raw.lost_in_japan, "lost_in_japan.mp3", "Shawn Mendes", "shawn_mendes.jpg");
    }

    private static void addSong(int id, String title, String author, String trackCover) {
        SongsProps.ids.add(id);

        SongsProps.songs.add(title);
        SongsProps.authors.add(author);
        SongsProps.covers.add(trackCover);
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
