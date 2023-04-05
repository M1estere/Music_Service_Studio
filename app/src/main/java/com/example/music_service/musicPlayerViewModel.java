package com.example.music_service;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.music_service.model.globals.PlaylistSystem;
import com.example.music_service.model.globals.Convert;
import com.example.music_service.model.globals.Globs;
import com.example.music_service.model.globals.SongsProps;
import com.example.music_service.model.Player;
import com.example.music_service.model.Playlist;
import com.example.music_service.model.Song;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MusicPlayerViewModel extends BaseObservable {

    private View mainPlayer;

    private SlidingUpPanelLayout slider;

    private TextView mainSongTitle;
    private TextView miniSongTitle;

    private Activity activity;

    private String trackName;
    private String authorName;

    private String currentTrackDuration;

    private int maxProgress;
    private String currentProgress;
    private int progress;

    private SeekBar progressBar;

    private boolean isSeeking = false;

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

        mainSongTitle = activity.findViewById(R.id.song_title);
        mainSongTitle.setSelected(true);

        miniSongTitle = activity.findViewById(R.id.title_txt);
        miniSongTitle.setSelected(true);

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
                    isSeeking = true;
                    return;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Player.goTo(progress * 1000);
                    isSeeking = false;
                    updateUI();
                    return;
                }
            });
        }

        Globs.fillAllSongs();
        Player.setMusicPlayer(this);
        initSongs();

        new Thread() {
            public void run() {
                while (Set == true) {
                    try {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if (Player.isPlay() == true) {
                                    if (isSeeking == false) {
                                        setProgress(Player.getCurrentPos() / 1000);
                                        setCurrentProgress(Convert.GetTimeFromSeconds(Player.getCurrentPos()));
                                    } else {
                                        setCurrentProgress(Convert.GetTimeFromSeconds(
                                                (Math.abs((progressBar.getMax() - progressBar.getProgress()) - progressBar.getMax())) * 1000)
                                        );
                                    }
                                    checkProgression();
                                } else {
                                    if (isSeeking == true)
                                        setCurrentProgress(Convert.GetTimeFromSeconds(
                                                (Math.abs((progressBar.getMax() - progressBar.getProgress()) -progressBar.getMax()) ) * 1000 )
                                        );
                                    else
                                        setCurrentProgress(Convert.GetTimeFromSeconds(
                                                (Math.abs((progressBar.getMax() - progressBar.getProgress()) - progressBar.getMax())) * 1000)
                                        );
                                }
                            }
                        });

                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        mainPlayer = activity.findViewById(R.id.main_player);

        slider = activity.findViewById(R.id.sliding_layout);
        slider.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                View navBar = activity.findViewById(R.id.nav_bar);

                System.out.printf("Height: %d\n", navBar.getHeight());
                navBar.animate().translationY(navBar.getHeight() - (1 - slideOffset) * navBar.getHeight()).setDuration(100);

                View miniPlayerView = activity.findViewById(R.id.mini_player);
                miniPlayerView.setAlpha(1 - (slideOffset * 2));

                mainPlayer.setAlpha( Math.abs((1 - slideOffset) - 1) );
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
//                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
//                    View navBar = activity.findViewById(R.id.nav_bar);
//                    navBar.setVisibility(View.GONE);
//                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
//                    View navBar = activity.findViewById(R.id.nav_bar);
//                    navBar.setVisibility(View.VISIBLE);
//                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
            activity.startService(new Intent(activity.getBaseContext(), OnClearFromRecentService.class));
        }

        CreateNotification.setViewModel(this);
        CreateNotification.createNotification(activity, Globs.currentSongs.get(Globs.currentTrackNumber),
                Globs.currentTrackNumber, Globs.currentSongs.size() - 1);

        updateUI();
    }

    private NotificationManager notificationManager;
    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Music Service", NotificationManager.IMPORTANCE_LOW);

            notificationManager = activity.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
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

        updateUI();
    }

    public void playSong() {
        Player.playSong();

        updateUI();
    }

    public void pauseSong() {
        Player.pause();

        updateUI();
    }

    public void queuePage() {
        Intent intent = new Intent(activity, QueueActivity.class);
        activity.startActivity(intent);
    }

    public void updateUI() {
        Globs.currentSongs.clear();

        for (String title : Player.getSongs())
            createSong(title, SongsProps.ids.get(SongsProps.songs.indexOf(title)));

        setTrackName(Globs.currentSongs.get(Globs.currentTrackNumber).getTitle());
        setAuthorName(Globs.currentSongs.get(Globs.currentTrackNumber).getArtist());

        setCurrentTrackDuration(Convert.GetTimeFromSeconds(Player.getDuration() - 1000));

        int max = Player.getDuration() / 1000;
        setMaxProgress(max);

        CreateNotification.destroyNotification();
        CreateNotification.setViewModel(this);
        CreateNotification.createNotification(activity, Globs.currentSongs.get(Globs.currentTrackNumber),
                Globs.currentTrackNumber, Globs.currentSongs.size() - 1);
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
