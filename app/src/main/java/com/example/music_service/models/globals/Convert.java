package com.example.music_service.models.globals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;

import java.util.ArrayList;

public class Convert {

    // convert 'break_through_it_all.mp3' to 'Break Through It All'
    public static String getTitleFromPath(String title) {
        int index = title.indexOf('.');
        if (index >= 0)
            title = title.substring(0, index);

        StringBuilder result = new StringBuilder();

        char prevChar = ' ';
        for (int i = 0; i < title.length(); i++) {
            if (Character.isWhitespace(prevChar)) // first iteration
            {
                prevChar = title.charAt(i);
                result.append(Character.toUpperCase(title.charAt(i)));
                continue;
            }
            if (prevChar == '_') {
                result.append(Character.toUpperCase(title.charAt(i)));
                prevChar = title.charAt(i);
                continue;
            }

            if (title.charAt(i) != '_') result.append(title.charAt(i));
            else result.append(' ');

            prevChar = title.charAt(i);
        }

        return result.toString();
    }

    // covert 'Break Through It All' to 'break_through_it_all.mp3'
    public static String getPathFromTitle(String title) {
        String result = title.toLowerCase();
        result = result.replace(' ', '_');

        if (!result.contains(".mp3"))
            result += ".mp3";

        return result;
    }

    // convert 225000 to 03:45
    public static String GetTimeFromSeconds(long mSeconds) {
        int seconds = (int) (mSeconds / 1000) % 60;
        int minutes = (int) ((mSeconds / (1000 * 60)) % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

    // create playlist out of songs list
    public static Playlist getPlaylistFromSongs(ArrayList<Song> songs, String name) {
        Playlist playlist = new Playlist(name);

        for (Song song : songs)
            playlist.addSong(song.getPath(), song.getUri());

        return playlist;
    }

    public static Bitmap getBitmapFromUri(Context context, String uri) {
        final Bitmap[] result = {null};

        Glide.with(context)
                .asBitmap()
                .load(Uri.parse(uri))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        result[0] = resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        return result[0];
    }

}
