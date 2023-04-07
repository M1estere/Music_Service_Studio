package com.example.music_service.model;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.example.music_service.MusicPlayerViewModel;
import com.example.music_service.R;
import com.example.music_service.model.globals.Convert;
import com.example.music_service.model.globals.Globs;
import com.example.music_service.model.globals.SongsProps;

import java.util.ArrayList;

public class Player {

    public enum RepeatingState { NoRepeat, RepeatPlaylist, RepeatTrack }
    public static RepeatingState playerRepeatingState = RepeatingState.NoRepeat;

    private static MediaPlayer musicPlayer;

    private static Context context;

    private static int currentTrackID;

    private static boolean playing = true;

    private static MusicPlayerViewModel musicPlayerViewModel;
    public static void setMusicPlayer(MusicPlayerViewModel m) {
        musicPlayerViewModel = m;
    }

    public static void startTrack() {
        int index = SongsProps.songs.indexOf(Convert.getPathFromTitle(Globs.getTitles().get(Globs.currentTrackNumber)));
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
        if (Globs.currentTrackNumber == Globs.currentSongs.size() - 1) Globs.currentTrackNumber = -1;

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
        Globs.currentSongs.clear();
        for (String title : titles)
            createSong(title, SongsProps.ids.get(SongsProps.songs.indexOf(title)));

        System.out.printf("globals count: %d", Globs.currentSongs.size());

        Globs.currentTrackNumber = 0;
        selectTrack(Globs.currentTrackNumber);

        musicPlayerViewModel.updateUI();
    }

    private static void createSong(String title, int id) {
        Song songToAdd = new Song(title, id);

        Globs.currentSongs.add(songToAdd);
    }

    public static void updateQueue(ArrayList<String> titles) {
        Globs.currentSongs.clear();
        for (String title : titles)
            createSong(title, SongsProps.ids.get(SongsProps.songs.indexOf(title)));

        Globs.currentTrackNumber = 0;
        selectTrack(Globs.currentTrackNumber);

        musicPlayerViewModel.updateUI();
    }

    public static void addToQueueEnd(String path)
    {
        String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
        if (currentTitle == path) return;

        if (Globs.currentSongs.contains(path) == true)
            deleteFromQueue(path);

        Globs.currentTrackNumber = Globs.currentSongs.indexOf(currentTitle);

        Globs.currentSongs.add(new Song(path, SongsProps.ids.get(SongsProps.songs.indexOf(path))));
        musicPlayerViewModel.updateUI();
    }

    public static void addNextToQueue(String path)
    {
        String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
        if (currentTitle == path) return;

        int oldLength = Globs.currentSongs.size();
        int index = Globs.currentTrackNumber + 1;
        String oldTrackTitle = currentTitle;

        if (Globs.getTitles().contains(path) == true)
        {
            int trackIndex = Globs.getTitles().indexOf(path);
            deleteFromQueue(path);

            index--;
            if (trackIndex > Globs.currentTrackNumber) index++;
        }
        Globs.currentSongs.add(index, new Song(path, SongsProps.ids.get(SongsProps.songs.indexOf(path))));
        Globs.currentTrackNumber = Globs.getTitles().indexOf(oldTrackTitle);

        musicPlayerViewModel.updateUI();
    }

    public static int deleteFromQueue(String name)
    {
        int deletableIndex = 0;

        for (int i = 0; i < Globs.currentSongs.size(); i++)
            if (Globs.getTitles().get(i).equals(name)) deletableIndex = i;

        Globs.removeSong(name);

        if (deletableIndex < Globs.currentTrackNumber)
            Globs.currentTrackNumber--;

        return Globs.currentTrackNumber;
    }

    public static String changeRepeatingState()
    {
        String returner = "";
        switch (playerRepeatingState)
        {
            case NoRepeat: {
                playerRepeatingState = RepeatingState.RepeatPlaylist;
                returner = "0";
                break;
            }
            case RepeatPlaylist: {
                playerRepeatingState = RepeatingState.RepeatTrack;
                returner = "1";
                break;
            }
            case RepeatTrack: {
                playerRepeatingState = RepeatingState.NoRepeat;
                returner = "";
                break;
            }
            default: {
                returner = "WTF";
                break;
            }
        }

        return returner;
    }

    public static void updatePlayer() {
        musicPlayerViewModel.updateUI();
    }

    public static void setContext(Context cont) {
        context = cont;
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
