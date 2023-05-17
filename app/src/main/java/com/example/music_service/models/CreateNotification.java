package com.example.music_service.models;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.music_service.R;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.viewModels.MusicPlayerViewModel;
import com.example.music_service.views.MainActivity;

public class CreateNotification {

    public static final String CHANNEL_ID = "channel1";

    public static Notification notification;
    public static MusicPlayerViewModel viewModel;

    private static Context context;
    private static MediaSessionCompat mediaSession;

    public static void setViewModel(MusicPlayerViewModel vm) {
        if (viewModel == null) viewModel = vm;
    }

    public static void createNotification(Context cont, Bitmap icon) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        context = cont;

        Song song = Globs.currentSongs.get(Globs.currentTrackNumber);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        mediaSession = new MediaSessionCompat(context, Globs.TAG);
        notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.play_40)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2))
                .setColor(Color.WHITE)
                .setColorized(true)
                .build();

        mediaSession.setMetadata(
                MediaMetadataCompat.fromMediaMetadata(new MediaMetadata.Builder()
                        .putString(MediaMetadata.METADATA_KEY_TITLE, song.getTitle())

                        .putString(MediaMetadata.METADATA_KEY_ARTIST, song.getArtist())

                        .putLong(MediaMetadata.METADATA_KEY_DURATION, Player.getDuration()) // 4
                        .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, icon)

                        .build())
        );

        mediaSession.setPlaybackState(
                PlaybackStateCompat.fromPlaybackState(new PlaybackState.Builder()
                        .setState(Player.isPlay() ? PlaybackState.STATE_PLAYING : PlaybackState.STATE_PAUSED, Player.getCurrentPos(), 1)
                        .setActions(PlaybackState.ACTION_SEEK_TO |
                                PlaybackState.ACTION_PLAY_PAUSE |
                                PlaybackState.ACTION_SKIP_TO_NEXT |
                                PlaybackState.ACTION_SKIP_TO_PREVIOUS)
                        .build())
        );

        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                viewModel.changePlayingState();
            }

            @Override
            public void onPause() {
                super.onPause();
                viewModel.changePlayingState();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                viewModel.nextSong();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                viewModel.previousSong();
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
                Player.goTo((int) (pos));
                viewModel.setProgress((int) (pos / 1000));
            }
        });

        mediaSession.setActive(true);

        notification.contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        context.startForegroundService(new Intent(context, Player.class));

        notificationManagerCompat.notify(1, notification);
    }

    public static void destroyNotification() {
        if (mediaSession == null) return;

        mediaSession.setActive(false);
        mediaSession.release();
    }
}