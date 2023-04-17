package com.example.music_service.viewModels;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.music_service.BR;
import com.example.music_service.CreateNotification;
import com.example.music_service.OnClearFromRecentService;
import com.example.music_service.activities.QueueActivity;
import com.example.music_service.R;
import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.models.Player;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MusicPlayerViewModel extends BaseObservable {
    private Activity activity;

    private CardView cover;

    private View mainPlayer;
    private View miniPlayerView;

    private SlidingUpPanelLayout slider;

    private TextView mainSongTitle;
    private TextView miniSongTitle;

    private String trackName;
    private String authorName;
    private String currentTrackDuration;
    private String currentProgress;

    private int maxProgress;
    private int progress;

    private SeekBar progressBar;
    private boolean isSeeking = false;

    public MusicPlayerViewModel(Activity mainActivity) {
        activity = mainActivity;
        initializePlayer();

        setProgressBarFunctionality();
        setSlidingUpPanelFunctionality();

        startPlayerThread();

        if (Player.getMusicPlayer() != null) updateUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
            activity.startService(new Intent(activity.getBaseContext(), OnClearFromRecentService.class));
        }
    }

    private void findAllIds() {
        cover = activity.findViewById(R.id.cover);

        mainSongTitle = activity.findViewById(R.id.song_title);
        miniSongTitle = activity.findViewById(R.id.title_txt);

        miniPlayerView = activity.findViewById(R.id.mini_player);
        mainPlayer = activity.findViewById(R.id.main_player);

        progressBar = activity.findViewById(R.id.progress_bar);
        slider = activity.findViewById(R.id.sliding_layout);
    }

    private void startElementsSetup() {
        mainSongTitle.setSelected(true);
        miniSongTitle.setSelected(true);

        miniPlayerView.setVisibility(View.GONE);
        mainPlayer.setVisibility(View.GONE);

        slider.setTouchEnabled(false);
        slider.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    private void initializePlayer() {
        findAllIds();
        startElementsSetup();

        Globs.fillAllSongs();
        Player.setMusicPlayer(this);
        Player.setContext(activity);

        FavouriteMusic.loadCollectionFromFile();
    }

    private void setProgressBarFunctionality() {
        if (progressBar == null) return;
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Player.goTo(progress * 1000);
                isSeeking = false;
                updateUI();
            }
        });
    }

    private void startPlayerThread() {
        final boolean Set = true;
        new Thread() {
            public void run() {
                while (Set == true) {
                    try {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (Player.getMusicPlayer() != null) {
                                    if (slider.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)
                                        slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

                                    if (miniPlayerView.getVisibility() == View.GONE) miniPlayerView.setVisibility(View.VISIBLE);
                                    if (mainPlayer.getVisibility() == View.GONE) mainPlayer.setVisibility(View.VISIBLE);
                                    if (slider.isTouchEnabled() == false) slider.setTouchEnabled(true);

                                    if (Player.isPlay() == true) {
                                        if (isSeeking == false) {
                                            setProgress(Player.getCurrentPos() / 1000);
                                            setCurrentProgress(Convert.GetTimeFromSeconds(Player.getCurrentPos()));
                                        } else {
                                            setCurrentProgress(Convert.GetTimeFromSeconds(
                                                    (Math.abs((progressBar.getMax() - progressBar.getProgress()) - progressBar.getMax())) * 1000L)
                                            );
                                        }
                                        checkProgression();
                                    } else {
                                        if (isSeeking == true)
                                            setCurrentProgress(Convert.GetTimeFromSeconds(
                                                    (Math.abs((progressBar.getMax() - progressBar.getProgress()) - progressBar.getMax())) * 1000L)
                                            );
                                        else
                                            setCurrentProgress(Convert.GetTimeFromSeconds(
                                                    (Math.abs((progressBar.getMax() - progressBar.getProgress()) - progressBar.getMax())) * 1000L)
                                            );
                                    }
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
    }

    private void setSlidingUpPanelFunctionality() {
        if (slider == null) return;
        slider.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                View navBar = activity.findViewById(R.id.nav_bar);

                navBar.animate().translationY(navBar.getHeight() - (1 - slideOffset) * navBar.getHeight()).setDuration(100);

                miniPlayerView.setAlpha(1 - (slideOffset * 2));

                mainPlayer.setAlpha(Math.abs((1 - slideOffset) - 1));
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {   }
        });
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Music Service", NotificationManager.IMPORTANCE_LOW);

            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

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

    @Bindable
    public String getTrackName() {
        return trackName;
    }
    public void setTrackName(String trackName) {
        this.trackName = trackName;

        notifyPropertyChanged(BR.trackName);
    }

    @Bindable
    public String getAuthorName() {
        return authorName;
    }
    private void setAuthorName(String authorName) {
        this.authorName = authorName;

        notifyPropertyChanged(BR.authorName);
    }

    private void checkProgression() {
        if (Player.getCurrentPos() >= Player.getDuration() - 2000)
            nextSong();
    }

    /*Primary Song Controls*/
    public void previousSong() {
        Globs.currentTrackNumber = Player.previousSong();

        updateUI();
        setProgress(0);
    }

    public void changePlayingState() {
        if (Player.isPlay()) pauseSong();
        else playSong();

        updateUI();
    }

    public void nextSong() {
        Globs.currentTrackNumber = Player.nextSong();

        updateUI();
        setProgress(0);
    }

    private void playSong() {
        cover.animate().scaleX(1f).scaleY(1f)
                .setDuration(150)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .start();
        Player.playSong();

        updateUI();
    }

    private void pauseSong() {
        cover.animate().scaleX(0.85f).scaleY(0.85f)
                .setDuration(200)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .start();
        Player.pause();

        updateUI();
    }

    /*Additive Song Controls*/
    public void changeRepeatingState() {
        Player.changeRepeatingState();
    }

    public void savePlaylist() {

    }

    public void favouriteTrack() {
        FavouriteMusic.addToFavourites(Globs.currentSongs.get(Globs.currentTrackNumber).getTitle(), activity);
    }

    public void queuePage() {
        Intent intent = new Intent(activity, QueueActivity.class);
        activity.startActivity(intent);
    }

    public void updateUI() {
        setTrackName(Globs.currentSongs.get(Globs.currentTrackNumber).getTitle());
        setAuthorName(Globs.currentSongs.get(Globs.currentTrackNumber).getArtist());

        setCurrentTrackDuration(Convert.GetTimeFromSeconds(Player.getDuration() - 1000));

        setMaxProgress(Player.getDuration() / 1000);

        CreateNotification.destroyNotification();
        CreateNotification.setViewModel(this);
        CreateNotification.createNotification(activity, Globs.currentSongs.get(Globs.currentTrackNumber),
                Globs.currentTrackNumber, Globs.currentSongs.size() - 1);
    }
}