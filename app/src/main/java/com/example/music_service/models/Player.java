package com.example.music_service.models;

import android.content.Context;

import com.example.music_service.models.data.SongsProps;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.viewModels.MusicPlayerViewModel;
import com.example.music_service.viewModels.QueueActivityViewModel;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import java.util.ArrayList;

public class Player {

    public static RepeatingState playerRepeatingState = RepeatingState.NoRepeat;
    private static Context context;
    private static ExoPlayer musicPlayer = null;
    private static boolean musicPaused = false;
    private static MusicPlayerViewModel musicPlayerViewModel;
    private static boolean isPrepared = false;
    private static QueueActivityViewModel queueActivityViewModel;

    public static boolean isIsPrepared() {
        return isPrepared;
    }

    public static boolean checkPrepared() {
        return isPrepared;
    }

    public static void setQueueActivityViewModel(QueueActivityViewModel q) {
        queueActivityViewModel = q;
    }

    public static void startTrack() {
        isPrepared = false;
        if (musicPlayer != null) {
            musicPlayer.setPlayWhenReady(false);
            musicPlayer.stop();
            musicPlayer.release();

            musicPlayer = null;
        }

        musicPlayerViewModel.setProgress(0);

        int index = SongsProps.songs.indexOf(Convert.getPathFromTitle(Globs.getTitles().get(Globs.currentTrackNumber)));
        String currentTrackUri = SongsProps.uris.get(index);

        musicPlayer = new ExoPlayer.Builder(context)
                .build();

        MediaItem mediaItem = MediaItem.fromUri(currentTrackUri);
        musicPlayer.addMediaItem(mediaItem);

        musicPlayer.addListener(new com.google.android.exoplayer2.Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == ExoPlayer.STATE_READY) {
                    isPrepared = true;
                    musicPlayer.setPlayWhenReady(!musicPaused);

                    musicPlayerViewModel.togglePauseLoading();
                    musicPlayerViewModel.updateUI(true);
                }
            }
        });

        musicPlayer.prepare();
    }

    public static ExoPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static void setMusicPlayer(MusicPlayerViewModel m) {
        musicPlayerViewModel = m;
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
        musicPaused = false;
        musicPlayer.play();
    }

    public static void pause() {
        musicPaused = true;
        musicPlayer.pause();
    }

    public static int nextSong() {
        if (Globs.currentTrackNumber == Globs.currentSongs.size() - 1)
            Globs.currentTrackNumber = -1;

        Globs.currentTrackNumber++;

        selectTrack(Globs.currentTrackNumber);

        return Globs.currentTrackNumber;
    }

    public static int nextSongAfterEnd() {
        if (playerRepeatingState == RepeatingState.RepeatTrack) {
            selectTrack(Globs.currentTrackNumber);
            return Globs.currentTrackNumber;
        }

        if ((Globs.currentTrackNumber == Globs.currentSongs.size() - 1) && playerRepeatingState == RepeatingState.RepeatPlaylist) {
            Globs.currentTrackNumber = -1;
        } else if ((Globs.currentTrackNumber == Globs.currentSongs.size() - 1) && playerRepeatingState != RepeatingState.RepeatPlaylist) {
            Globs.currentTrackNumber = 0;
            selectTrack(Globs.currentTrackNumber);

            return -5;
        }

        Globs.currentTrackNumber++;
        selectTrack(Globs.currentTrackNumber);

        return Globs.currentTrackNumber;
    }

    public static void selectTrack(int index) {
        musicPlayerViewModel.togglePauseLoading();

        Globs.currentTrackNumber = index;
        if (musicPlayer != null) musicPlayer.stop();

        startTrack();

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI(true);
        if (musicPaused) musicPlayer.pause();
    }

    private static void createSong(String title, String uri) {
        Song songToAdd = new Song(title, uri);

        Globs.currentSongs.add(songToAdd);
    }

    public static void updateQueue(ArrayList<String> titles) {
        Globs.currentSongs.clear();
        for (String title : titles)
            createSong(title, SongsProps.uris.get(SongsProps.songs.indexOf(title)));

        Globs.currentTrackNumber = 0;
        selectTrack(Globs.currentTrackNumber);

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI(true);
    }

    public static void updateQueue(ArrayList<String> titles, int startIndex) {
        Globs.currentSongs.clear();
        for (String title : titles) {
            createSong(title, SongsProps.uris.get(SongsProps.songs.indexOf(title)));
        }

        Globs.currentTrackNumber = startIndex;
        selectTrack(Globs.currentTrackNumber);
    }

    public static void addToQueueEnd(String title) {
        String path = Convert.getPathFromTitle(title);

        String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
        String currentPath = Convert.getPathFromTitle(currentTitle);
        if (currentPath.equals(path)) return;

        if (Globs.getSongNumber(path) != -1)
            deleteFromQueue(path);

        Globs.currentSongs.add(new Song(path, SongsProps.uris.get(SongsProps.songs.indexOf(path))));

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI(true);
        if (queueActivityViewModel != null) queueActivityViewModel.updateQueue();
    }

    public static void addNextToQueue(String title) {
        String path = Convert.getPathFromTitle(title);

        String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
        String currentPath = Convert.getPathFromTitle(currentTitle);
        if (currentPath.equals(path)) return;

        int index = Globs.currentTrackNumber + 1;

        title = Convert.getTitleFromPath(title);
        if (Globs.getTitles().contains(title)) {
            int trackIndex = Globs.getTitles().indexOf(title);
            deleteFromQueue(path);

            index--;
            if (trackIndex > Globs.currentTrackNumber) index++;
        }
        Globs.currentSongs.add(index, new Song(path, SongsProps.uris.get(SongsProps.songs.indexOf(path))));
        Globs.currentTrackNumber = Globs.getTitles().indexOf(currentTitle);

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI(true);
        if (queueActivityViewModel != null) queueActivityViewModel.updateQueue();
    }

    public static void insertInQueue(String title, int index) {
        title = Convert.getTitleFromPath(title);
        String path = Convert.getPathFromTitle(title);

        String current = Globs.getTitlesPaths().get(Globs.currentTrackNumber);

        if (current.equals(path)) return;

        if (Globs.getTitles().contains(title)) {
            int trackIndex = Globs.getTitles().indexOf(title);
            deleteFromQueue(path);

            index--;
            if (trackIndex > Globs.currentTrackNumber) index++;
        }

        if (index > Globs.currentSongs.size()) index = Globs.currentSongs.size();
        Globs.currentSongs.add(index, new Song(path, SongsProps.uris.get(SongsProps.songs.indexOf(path))));
        Globs.currentTrackNumber = Globs.getTitlesPaths().indexOf(current);

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI(true);
        if (queueActivityViewModel != null) queueActivityViewModel.updateQueue();
    }

    public static void addNextToQueue(Playlist playlist) {
        ArrayList<String> titles = playlist.getSongTitles();

        for (int i = 0; i < titles.size(); i++) {
            int count = Globs.currentTrackNumber + (i + 1);
            insertInQueue(titles.get(i), count);
        }
    }

    public static void addToQueueEnd(Playlist playlist) {
        ArrayList<String> titles = playlist.getSongTitles();

        for (int i = 0; i < titles.size(); i++) {
            int count = Globs.currentSongs.size() + i;

            insertInQueue(titles.get(i), count);
        }
    }

    public static void deleteFromQueue(String name) {
        String path = Convert.getPathFromTitle(name);

        String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
        String currentPath = Convert.getPathFromTitle(currentTitle);
        if (currentPath.equals(path)) return;

        int deletableIndex = 0;

        for (int i = 0; i < Globs.currentSongs.size(); i++)
            if (Globs.getTitles().get(i).equals(path)) deletableIndex = i;

        Globs.removeSong(path);

        if (deletableIndex < Globs.currentTrackNumber)
            Globs.currentTrackNumber--;

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI(true);
        if (queueActivityViewModel != null) queueActivityViewModel.updateQueue();
    }

    public static String changeRepeatingState() {
        String returner = "";
        switch (playerRepeatingState) {
            case NoRepeat:
                playerRepeatingState = RepeatingState.RepeatPlaylist;
                returner = "Repeating playlist";
                break;
            case RepeatPlaylist:
                playerRepeatingState = RepeatingState.RepeatTrack;
                returner = "Repeating track";
                break;
            case RepeatTrack:
                playerRepeatingState = RepeatingState.NoRepeat;
                returner = "No repeat";
                break;
            default:
                returner = "Error occured";
                break;
        }

        return returner;
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
        musicPlayerViewModel.updateUI(false);
    }

    public static float getProgress() {
        return (musicPlayer.getCurrentPosition() / (float) musicPlayer.getDuration());
    }

    public static int getDuration() {
        int duration = (int) musicPlayer.getDuration() - 1000;
        return Math.max(duration, 2);
    }

    public static int getCurrentPos() {
        return (int) musicPlayer.getCurrentPosition();
    }

    public static boolean isPlay() {
        return !musicPaused;
    }

    public enum RepeatingState {NoRepeat, RepeatPlaylist, RepeatTrack}
}
