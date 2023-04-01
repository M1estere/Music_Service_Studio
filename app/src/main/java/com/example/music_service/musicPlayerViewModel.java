package com.example.music_service;

import android.app.Activity;
import android.content.Intent;
import android.widget.SeekBar;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.music_service.model.globals.PlaylistSystem;
import com.example.music_service.model.globals.Convert;
import com.example.music_service.model.globals.Globs;
import com.example.music_service.model.globals.SongsProps;
import com.example.music_service.model.Player;
import com.example.music_service.model.Playlist;
import com.example.music_service.model.Song;

public class MusicPlayerViewModel extends BaseObservable {

    private Activity activity;

    private String trackName;
    private String authorName;

    private String currentTrackDuration;

    private int maxProgress;
    private String currentProgress;
    private int progress;

    private SeekBar progressBar;

    @Bindable
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;

        notifyPropertyChanged(BR.progress);
    }

    @Bindable
    public String getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(String currentProgress) {
        this.currentProgress = currentProgress;

        notifyPropertyChanged(BR.currentProgress);
    }

    @Bindable
    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;

        notifyPropertyChanged(BR.maxProgress);
    }

    @Bindable
    public String getCurrentTrackDuration() {
        return currentTrackDuration;
    }

    public void setCurrentTrackDuration(String currentTrackDuration) {
        this.currentTrackDuration = currentTrackDuration;

        notifyPropertyChanged(BR.currentTrackDuration);
    }

    public MusicPlayerViewModel(Activity mainActivity, boolean refill) {
        activity = mainActivity;

        final boolean Set = true;

        progressBar = activity.findViewById(R.id.progress_bar);
        if (progressBar != null) {
            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                private int progress;

                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    progress = i;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    return;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Player.goTo(progress * 1000);
                    return;
                }
            });
        }

        if (refill == true) {
            Globs.fillAllSongs();
            initSongs();
        }

        new Thread() {
            public void run() {
                while (Set == true) {
                    try {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                setCurrentProgress(Convert.GetTimeFromSeconds(Player.getCurrentPos()));
                                if (Player.isPlay() == true) {
                                    setProgress(Player.getCurrentPos() / 1000);

                                    checkProgression();

                                    updateUI();
                                }
                            }
                        });

                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        updateUI();
    }

    @Bindable
    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;

        notifyPropertyChanged(BR.trackName);
    }

    private void checkProgression() {
        if (Player.getCurrentPos() >= Player.getDuration() - 2000)
            nextSong();
    }

    @Bindable
    public String getAuthorName() {
        return authorName;
    }

    private void setAuthorName(String authorName) {
        this.authorName = authorName;

        notifyPropertyChanged(BR.authorName);
    }

    public void previousSong() {
        Globs.currentTrackNumber = Player.previousSong();

        updateUI();
    }

    public void nextSong() {
        Globs.currentTrackNumber = Player.nextSong();

        updateUI();
    }

    public void changePlayingState() {
        if (Player.isPlay()) pauseSong();
        else playSong();
    }

    private void playSong() {
        Player.playSong();
    }

    private void pauseSong() {
        Player.pause();
    }

    public void queuePage() {
        Intent intent = new Intent(activity, QueueActivity.class);
        activity.startActivity(intent);
    }

    public void homePage() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public void backToPlayer() {
        activity.onBackPressed();
    }

    private void updateUI() {
        Globs.currentSongs.clear();

        for (String title : Player.getSongs())
            createSong(title, SongsProps.ids.get(SongsProps.songs.indexOf(title)));

        setTrackName(Globs.currentSongs.get(Globs.currentTrackNumber).getTitle());
        setAuthorName(Globs.currentSongs.get(Globs.currentTrackNumber).getArtist());

        setCurrentTrackDuration(Convert.GetTimeFromSeconds(Player.getDuration() - 1000));

        int max = Player.getDuration() / 1000;
        setMaxProgress(max);
    }

    public void changeRepeatingState() {

    }

    public void savePlaylist() {

    }

    public void favouriteTrack() {

    }

    private void createSong(String title, int id) {
        Song songToAdd = new Song(title, id);

        Globs.currentSongs.add(songToAdd);
    }

    private void initSongs() {
        Player.setContext(activity);

        Playlist playlist = new Playlist("Start List");

        PlaylistSystem.fillOnePlaylist(20, playlist);

        Player.setQueue(playlist.getSongTitles());
    }
}
