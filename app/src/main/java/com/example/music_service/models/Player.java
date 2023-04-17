package com.example.music_service.models;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.music_service.viewModels.MusicPlayerViewModel;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.SongsProps;
import com.example.music_service.viewModels.QueueActivityViewModel;

import java.util.ArrayList;

public class Player {
    public enum RepeatingState { NoRepeat, RepeatPlaylist, RepeatTrack }

    private static Context context;
    private static MediaPlayer musicPlayer = null;

    public static RepeatingState playerRepeatingState = RepeatingState.NoRepeat;

    private static boolean musicPaused = false;

    private static MusicPlayerViewModel musicPlayerViewModel;
    public static void setMusicPlayer(MusicPlayerViewModel m) {
        musicPlayerViewModel = m;
    }

    private static QueueActivityViewModel queueActivityViewModel;
    public static void setQueueActivityViewModel(QueueActivityViewModel q) {
        queueActivityViewModel = q;
    }

    public static void startTrack() {
        int index = SongsProps.songs.indexOf(Convert.getPathFromTitle(Globs.getTitles().get(Globs.currentTrackNumber)));
        int currentTrackID = SongsProps.ids.get(index);

        if (musicPlayer != null) {
            musicPlayer.reset();
            musicPlayer.release();
        }

        musicPlayer = MediaPlayer.create(context, currentTrackID);
        justPlay();
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

    private static void justPlay() {
        musicPlayer.start();
    }

    public static void playSong() {
        musicPaused = false;
        musicPlayer.start();
    }

    public static void pause() {
        musicPaused = true;
        musicPlayer.pause();
    }

    public static int nextSong() {
        if (Globs.currentTrackNumber == Globs.currentSongs.size() - 1) Globs.currentTrackNumber = -1;

        Globs.currentTrackNumber++;

        selectTrack(Globs.currentTrackNumber);

        return Globs.currentTrackNumber;
    }

    public static void selectTrack(int index) {
        Globs.currentTrackNumber = index;
        if (musicPlayer != null) musicPlayer.stop();

        startTrack();

        musicPlayer.start();
        if (musicPaused) musicPlayer.pause();

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

    public static void updateQueue(ArrayList<String> titles, int startIndex) {
        Globs.currentSongs.clear();
        for (String title : titles)
            createSong(title, SongsProps.ids.get(SongsProps.songs.indexOf(title)));

        Globs.currentTrackNumber = startIndex;
        selectTrack(Globs.currentTrackNumber);

        musicPlayerViewModel.updateUI();
    }

    public static void addToQueueEnd(String title)
    {
        String path = Convert.getPathFromTitle(title);

        String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
        String currentPath = Convert.getPathFromTitle(currentTitle);
        if (currentPath.equals(path)) return;

        if (Globs.getSongNumber(path) != -1)
            deleteFromQueue(path);

        Globs.currentSongs.add(new Song(path, SongsProps.ids.get(SongsProps.songs.indexOf(path))));

        musicPlayerViewModel.updateUI();
        queueActivityViewModel.updateQueue();
    }

    public static void addNextToQueue(String title)
    {
        String path = Convert.getPathFromTitle(title);

        String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
        String currentPath = Convert.getPathFromTitle(currentTitle);
        if (currentPath.equals(path)) return;

        int index = Globs.currentTrackNumber + 1;

        if (Globs.getTitles().contains(title))
        {
            int trackIndex = Globs.getTitles().indexOf(title);
            deleteFromQueue(path);

            index--;
            if (trackIndex > Globs.currentTrackNumber) index++;
        }
        Globs.currentSongs.add(index, new Song(path, SongsProps.ids.get(SongsProps.songs.indexOf(path))));
        Globs.currentTrackNumber = Globs.getTitles().indexOf(currentTitle);

        musicPlayerViewModel.updateUI();
        queueActivityViewModel.updateQueue();
    }

    public static void deleteFromQueue(String name)
    {
        String path = Convert.getPathFromTitle(name);

        String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
        String currentPath = Convert.getPathFromTitle(currentTitle);
        if (currentPath.equals(path)) return;

        int deletableIndex = 0;

        for (int i = 0; i < Globs.currentSongs.size(); i++)
            if (Globs.getTitles().get(i).equals(path)) deletableIndex = i;

        Globs.removeSong(path);
        System.out.printf("Removing %s\n", path);

        if (deletableIndex < Globs.currentTrackNumber)
            Globs.currentTrackNumber--;

        musicPlayerViewModel.updateUI();
        queueActivityViewModel.updateQueue();
    }

    public static void changeRepeatingState()
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
    }

    public static void drop() {
        if (musicPlayer == null) return;

        if (musicPlayer.isPlaying())
            musicPlayer.stop();

        musicPlayer.release();
        musicPlayer = null;
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
        return !musicPaused;
    }
}