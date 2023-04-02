package com.example.music_service.model;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.example.music_service.MusicPlayerViewModel;
import com.example.music_service.model.globals.Globs;
import com.example.music_service.model.globals.SongsProps;

import java.util.ArrayList;

public class Player {

    private static MediaPlayer musicPlayer;
    private static ArrayList<String> songTitles = new ArrayList<>();

    private static Context context;

    private static int currentTrackID;

    private static boolean playing = true;

    private static MusicPlayerViewModel musicPlayerViewModel;
    public static void setMusicPlayer(MusicPlayerViewModel m) {
        musicPlayerViewModel = m;
    }

    public static void startTrack() {
        int index = SongsProps.songs.indexOf(songTitles.get(Globs.currentTrackNumber));
        currentTrackID = SongsProps.ids.get(index);

        if (musicPlayer != null) {
            musicPlayer.reset();
            musicPlayer.release();
        }
        musicPlayer = MediaPlayer.create(context, currentTrackID);
        playSong();
    }

    public static MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static int previousSong() {
        if (getProgress() >= 0.2f) {
            selectTrack(Globs.currentTrackNumber);
            return Globs.currentTrackNumber;
        }

        if (Globs.currentTrackNumber == 0) selectTrack(Globs.currentTrackNumber);
        else {
            Globs.currentTrackNumber -= 1;
            selectTrack(Globs.currentTrackNumber);
        }

        return Globs.currentTrackNumber;
    }

    public static void playSong() {
        musicPlayer.start();

        playing = true;
    }

    public static void pause() {
        musicPlayer.pause();

        playing = false;
    }

    public static int nextSong() {
        if (Globs.currentTrackNumber == songTitles.size() - 1) Globs.currentTrackNumber = -1;

        Globs.currentTrackNumber++;

        selectTrack(Globs.currentTrackNumber);

        return Globs.currentTrackNumber;
    }

    public static void selectTrack(int index) {
        boolean oldState = playing;

        Globs.currentTrackNumber = index;
        if (musicPlayer != null) musicPlayer.stop();

        startTrack();
        musicPlayer.start();

        if (oldState == false) musicPlayer.pause();

        musicPlayerViewModel.updateUI();
    }

    public static void setQueue(@NonNull ArrayList<String> titles) {
        songTitles.clear();
        songTitles = (ArrayList<String>) titles.clone();

        Globs.currentTrackNumber = 0;
        selectTrack(Globs.currentTrackNumber);

        musicPlayerViewModel.updateUI();
    }

    public static void updateQueue(ArrayList<String> titles) {
        songTitles.clear();
        songTitles = (ArrayList<String>) titles.clone();

        Globs.currentTrackNumber = 0;
        selectTrack(Globs.currentTrackNumber);

        musicPlayerViewModel.updateUI();
    }

    public static void setContext(Context cont) {
        context = cont;
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
        return musicPlayer.getDuration() - 1000;
    }

    public static int getCurrentPos() {
        return musicPlayer.getCurrentPosition();
    }

    public static boolean isPlay() {
        return musicPlayer.isPlaying();
    }
}
