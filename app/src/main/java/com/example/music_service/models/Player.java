package com.example.music_service.models;

import android.content.Context;
import android.widget.Toast;

import com.example.music_service.viewModels.MusicPlayerViewModel;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.SongsProps;
import com.example.music_service.viewModels.QueueActivityViewModel;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFile;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.gms.drive.DriveId;

import java.util.ArrayList;

public class Player {

    public enum RepeatingState { NoRepeat, RepeatPlaylist, RepeatTrack }

    private static Context context;
    private static ExoPlayer musicPlayer = null;

    public static RepeatingState playerRepeatingState = RepeatingState.NoRepeat;

    private static boolean musicPaused = false;

    private static MusicPlayerViewModel musicPlayerViewModel;
    public static void setMusicPlayer(MusicPlayerViewModel m) {
        musicPlayerViewModel = m;
    }

    private static boolean isPrepared = false;

    public static boolean checkPrepared() {
        return isPrepared;
    }

    private static QueueActivityViewModel queueActivityViewModel;
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

        System.out.println("Currently chosen: " + currentTrackUri);
        musicPlayer.addListener(new com.google.android.exoplayer2.Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == ExoPlayer.STATE_READY) {
                    isPrepared = true;
                    musicPlayer.setPlayWhenReady(!musicPaused);
                    musicPlayerViewModel.updateUI();

                    Toast.makeText(context, "Live with: " + Globs.currentSongs.get(Globs.currentTrackNumber).getTitle().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        musicPlayer.prepare();
    }

    public static ExoPlayer getMusicPlayer() {
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
        musicPaused = false;
        musicPlayer.play();
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

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI();
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

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI();
    }

    public static void updateQueue(ArrayList<String> titles, int startIndex) {
        Globs.currentSongs.clear();
        for (String title : titles) {
            createSong(title, SongsProps.uris.get(SongsProps.songs.indexOf(title)));
        }

        Globs.currentTrackNumber = startIndex;
        selectTrack(Globs.currentTrackNumber);
    }

    public static void addToQueueEnd(String title)
    {
        String path = Convert.getPathFromTitle(title);

        String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
        String currentPath = Convert.getPathFromTitle(currentTitle);
        if (currentPath.equals(path)) return;

        if (Globs.getSongNumber(path) != -1)
            deleteFromQueue(path);

        Globs.currentSongs.add(new Song(path, SongsProps.uris.get(SongsProps.songs.indexOf(path))));

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI();
        if (queueActivityViewModel != null) queueActivityViewModel.updateQueue();
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
        Globs.currentSongs.add(index, new Song(path, SongsProps.uris.get(SongsProps.songs.indexOf(path))));
        Globs.currentTrackNumber = Globs.getTitles().indexOf(currentTitle);

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI();
        if (queueActivityViewModel != null) queueActivityViewModel.updateQueue();
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

        if (deletableIndex < Globs.currentTrackNumber)
            Globs.currentTrackNumber--;

        if (musicPlayerViewModel != null) musicPlayerViewModel.updateUI();
        if (queueActivityViewModel != null) queueActivityViewModel.updateQueue();
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
        int duration = (int)musicPlayer.getDuration() - 1000;
        return Math.max(duration, 2);
    }
    public static int getCurrentPos() {
        return (int)musicPlayer.getCurrentPosition();
    }
    public static boolean isPlay() {
        return !musicPaused;
    }
}
