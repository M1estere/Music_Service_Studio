package com.example.music_service.viewModels;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.music_service.BR;
import com.example.music_service.R;
import com.example.music_service.models.CreateNotification;
import com.example.music_service.models.CustomPlaylists;
import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.models.Player;
import com.example.music_service.models.data.SongsProps;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.viewModels.services.OnClearFromRecentService;
import com.example.music_service.views.QueueActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MusicPlayerViewModel extends BaseObservable {
    private final Activity activity;

    private CardView cover;

    private View mainPlayer;
    private View miniPlayerView;

    private ImageView mainCoverImage;
    private ImageView miniCoverImage;
    private ImageView heartImage;

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

    private ImageButton bigPause;
    private ImageButton smallPause;

    public MusicPlayerViewModel(Activity mainActivity) {
        activity = mainActivity;
        setIds();
        initializePlayer();

        startElementsSetup();
        startPlayerThread();

        if (Player.getMusicPlayer() != null) updateUI();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
            activity.startService(new Intent(activity.getBaseContext(), OnClearFromRecentService.class));
        }
    }

    private void setIds() {
        cover = activity.findViewById(R.id.cover);

        mainSongTitle = activity.findViewById(R.id.song_title);
        miniSongTitle = activity.findViewById(R.id.title_txt);

        heartImage = activity.findViewById(R.id.add_track_to_favs_button);

        miniPlayerView = activity.findViewById(R.id.mini_player);
        mainPlayer = activity.findViewById(R.id.main_player);

        mainCoverImage = activity.findViewById(R.id.big_track_cover);
        miniCoverImage = activity.findViewById(R.id.mini_track_cover);

        progressBar = activity.findViewById(R.id.progress_bar);
        slider = activity.findViewById(R.id.sliding_layout);

        bigPause = activity.findViewById(R.id.pause_button);
        smallPause = activity.findViewById(R.id.pause);
    }

    private void startElementsSetup() {
        mainSongTitle.setSelected(true);
        miniSongTitle.setSelected(true);

        miniPlayerView.setVisibility(View.GONE);
        mainPlayer.setVisibility(View.GONE);

        slider.setTouchEnabled(false);
        slider.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        setProgressBarFunctionality();
        setSlidingUpPanelFunctionality();
    }

    private void initializePlayer() {
        Player.setMusicPlayer(this);
        Player.setContext(activity);

        FavouriteMusic.loadCollectionFromFile();
        CustomPlaylists.loadPlaylists();
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
                setProgress(progress);
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
                while (Set) {
                    try {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (Player.getMusicPlayer() != null) {
                                    if (slider.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)
                                        slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

                                    if (miniPlayerView.getVisibility() == View.GONE)
                                        miniPlayerView.setVisibility(View.VISIBLE);
                                    if (mainPlayer.getVisibility() == View.GONE)
                                        mainPlayer.setVisibility(View.VISIBLE);
                                    if (!slider.isTouchEnabled())
                                        slider.setTouchEnabled(true);

                                    if (Player.isPlay()) {
                                        if (!isSeeking) {
                                            setProgress(Player.getCurrentPos() / 1000);
                                            setCurrentProgress(Convert.GetTimeFromSeconds(Player.getCurrentPos()));
                                        } else {
                                            setCurrentProgress(Convert.GetTimeFromSeconds(
                                                    (Math.abs((progressBar.getMax() - progressBar.getProgress()) - progressBar.getMax())) * 1000L)
                                            );
                                        }
                                        checkProgression();
                                    } else {
                                        if (isSeeking)
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
                BottomNavigationView navBar = activity.findViewById(R.id.nav_bar);
                navBar.animate().translationY(navBar.getHeight() - (1 - slideOffset) * navBar.getHeight()).setDuration(100);

                miniPlayerView.setAlpha(1 - (slideOffset * 2));
                mainPlayer.setAlpha(Math.abs((1 - slideOffset) - 1));
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                    updateUI();
            }
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

    public void setChangeStateButtonGfxId(int id) {
        if (id == R.drawable.play_80) {
            bigPause.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.play_80));
            smallPause.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.play_40));
        } else {
            bigPause.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.pause_80));
            smallPause.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.pause_40));
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
        if (!Player.checkPrepared()) return;
        if (Player.getCurrentPos() >= Player.getDuration())
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
        Player.nextSong();

        updateUI();
        setProgress(0);
    }

    private void playSong() {
        setChangeStateButtonGfxId(R.drawable.pause_80);

        cover.animate().scaleX(1f).scaleY(1f)
                .setDuration(150)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .start();
        Player.playSong();

        updateUI();
    }

    private void pauseSong() {
        setChangeStateButtonGfxId(R.drawable.play_80);

        cover.animate().scaleX(0.85f).scaleY(0.85f)
                .setDuration(200)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .start();
        Player.pause();

        updateUI();
    }

    /*Special Song Controls*/
    public void changeRepeatingState() {
        Toast.makeText(activity, "Current repeating state: " + Player.changeRepeatingState(), Toast.LENGTH_SHORT).show();
    }

    public void savePlaylist() {
        CustomPlaylists.savingPlaylist(activity, Globs.getTitlesPaths());
    }

    public void favouriteTrack() {
        boolean added = FavouriteMusic.addToFavourites(Globs.currentSongs.get(Globs.currentTrackNumber).getTitle(), activity);
        heartImage.setImageDrawable(added ? AppCompatResources.getDrawable(activity, R.drawable.heart_filled_40) : AppCompatResources.getDrawable(activity, R.drawable.heart_unfilled_40));
    }

    public void queuePage() {
        Intent intent = new Intent(activity, QueueActivity.class);
        activity.startActivity(intent);
    }

    public void updateUI() {
        if (Player.getMusicPlayer() == null) return;

        checkPausedState();

        setTrackName(Globs.currentSongs.get(Globs.currentTrackNumber).getTitle());
        setAuthorName(Globs.currentSongs.get(Globs.currentTrackNumber).getArtist());

        setCurrentTrackDuration(Convert.GetTimeFromSeconds(Player.getDuration() - 1000));
        setMaxProgress(Player.getDuration() / 1000);

        heartImage.setImageDrawable(FavouriteMusic.contains(Globs.currentSongs.get(Globs.currentTrackNumber).getTitle()) ? AppCompatResources.getDrawable(activity, R.drawable.heart_filled_40) : AppCompatResources.getDrawable(activity, R.drawable.heart_unfilled_40));

        Glide.with(cover)
                .load(SongsProps.getCurrentCover())
                .thumbnail(0.05f)
                .into(mainCoverImage);
        Glide.with(cover)
                .load(SongsProps.getCurrentCover())
                .thumbnail(0.05f)
                .into(miniCoverImage);

        createNotification();
    }

    public void updateUI(boolean create) {
        if (Player.getMusicPlayer() == null) return;

        checkPausedState();

        setTrackName(Globs.currentSongs.get(Globs.currentTrackNumber).getTitle());
        setAuthorName(Globs.currentSongs.get(Globs.currentTrackNumber).getArtist());

        setCurrentTrackDuration(Convert.GetTimeFromSeconds(Player.getDuration() - 1000));
        setMaxProgress(Player.getDuration() / 1000);
    }

    private void checkPausedState() {
        if (!Player.isPlay()) {
            setChangeStateButtonGfxId(R.drawable.play_80);

            cover.animate().scaleX(0.85f).scaleY(0.85f)
                    .setDuration(200)
                    .setInterpolator(new AnticipateOvershootInterpolator())
                    .start();
        } else {
            setChangeStateButtonGfxId(R.drawable.pause_80);

            cover.animate().scaleX(1f).scaleY(1f)
                    .setDuration(150)
                    .setInterpolator(new AnticipateOvershootInterpolator())
                    .start();
        }
    }

    private void createNotification() {
        //Bitmap largeIcon = Convert.getBitmapFromUri(activity, Globs.currentSongs.get(Globs.currentTrackNumber).getCover());

        Glide.with(activity)
                .asBitmap()
                .load(Uri.parse(Globs.currentSongs.get(Globs.currentTrackNumber).getCover()))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        CreateNotification.destroyNotification();
                        CreateNotification.setViewModel(MusicPlayerViewModel.this);
                        CreateNotification.createNotification(activity, Globs.currentSongs.get(Globs.currentTrackNumber),
                                Globs.currentTrackNumber, Globs.currentSongs.size() - 1, resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

    }

}
