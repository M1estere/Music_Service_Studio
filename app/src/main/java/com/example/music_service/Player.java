package com.example.music_service;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Player {

    private static MediaPlayer musicPlayer;
    private static ArrayList<String> songTitles = new ArrayList<>();

    private static Context context;

    private static int currentTrackNumber = 0;
    private static int currentTrackID;

    private static boolean playing = true;

    public static void setContext(Context cont) {
        context = cont;
    }

    public static void startTrack() {
        int index = SongsProps.songs.indexOf(songTitles.get(currentTrackNumber));
        currentTrackID = SongsProps.ids.get(index);

        musicPlayer = MediaPlayer.create(context, currentTrackID);
        playSong();
    }

    public static void playSong() {
        musicPlayer.start();

        playing = true;
    }

    public static int previousSong() {
        if (getProgress() >= 0.2f) {
            selectTrack(currentTrackNumber);
            return currentTrackNumber;
        }

        if (currentTrackNumber == 0) selectTrack(currentTrackNumber);
        else {
            currentTrackNumber -= 1;
            selectTrack(currentTrackNumber);
        }

        return currentTrackNumber;
    }

    public static void pause() {
        musicPlayer.pause();

        playing = false;
    }

    public static int nextSong() {
        if (currentTrackNumber == songTitles.size() - 1) currentTrackNumber = -1;

        currentTrackNumber++;

        selectTrack(currentTrackNumber);

        return currentTrackNumber;
    }

    public static void selectTrack(int index) {
        boolean oldState = playing;

        currentTrackNumber = index;
        if (musicPlayer != null) musicPlayer.stop();

        startTrack();
        musicPlayer.start();
        if (oldState == false) musicPlayer.pause();
    }

    public static void setQueue(@NonNull ArrayList<String> titles) {
        songTitles.clear();
        songTitles = (ArrayList<String>) titles.clone();

        currentTrackNumber = 0;
        selectTrack(currentTrackNumber);
    }

    public static ArrayList<String> getSongs() {
        return songTitles;
    }

    public static void goTo(int seekPosition) {
        musicPlayer.seekTo(seekPosition);
    }

    public static float getProgress() {
        return (musicPlayer.getCurrentPosition() / (float) musicPlayer.getDuration());
    }

    public static int getDuration() {
        return musicPlayer.getDuration();
    }

    public static int getCurrentPos() {
        return musicPlayer.getCurrentPosition();
    }

    public static boolean isPlay() {
        return musicPlayer.isPlaying();
    }
}
