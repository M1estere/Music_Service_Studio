package com.example.music_service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.music_service.model.Player;
import com.example.music_service.model.Song;

public class CreateNotification {

    public static final String CHANNEL_ID = "channel1";

    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_NEXT = "action_next";

    public static Notification notification;

    private static MediaSessionCompat mediaSession;

    public static MusicPlayerViewModel viewModel;
    public static void setViewModel(MusicPlayerViewModel vm) {
        viewModel = vm;
    }

    public static void createNotification(Context context, Song song, int position, int size) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            mediaSession = new MediaSessionCompat(context, "tag");

            Bitmap iconB = BitmapFactory.decodeResource(context.getResources(), R.drawable.shawn);

            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.oneokrock)
                    .setLargeIcon(iconB)
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
                            //.putString(
                            //        MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, currentTrack.albumArtUri)

                            .putLong(MediaMetadata.METADATA_KEY_DURATION, Player.getDuration()) // 4
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

                    System.out.println("Continued");
                    viewModel.changePlayingState();
                }

                @Override
                public void onPause() {
                    super.onPause();

                    System.out.println("Paused");
                    viewModel.changePlayingState();
                }

                @Override
                public void onSkipToNext() {
                    super.onSkipToNext();

                    System.out.println("Next");
                    viewModel.nextSong();
                }

                @Override
                public void onSkipToPrevious() {
                    super.onSkipToPrevious();

                    System.out.println("Previous");
                    viewModel.previousSong();
                }

                @Override
                public void onSeekTo(long pos) {
                    super.onSeekTo(pos);

                    System.out.println("Moved");
                    Player.goTo((int) (pos));
                    viewModel.updateUI();
                    viewModel.setProgress((int) (pos / 1000));
                }
            });

            mediaSession.setActive(true);

            notificationManagerCompat.notify(1, notification);
        }
    }

    public static void destroyNotification() {
        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }
}