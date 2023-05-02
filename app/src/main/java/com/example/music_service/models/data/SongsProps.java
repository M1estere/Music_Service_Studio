package com.example.music_service.models.data;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.music_service.models.Song;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SongsProps {

    public static ArrayList<String> songs = new ArrayList<>();
    public static ArrayList<String> authors = new ArrayList<>();
    public static ArrayList<String> uris = new ArrayList<>();
    public static ArrayList<String> covers = new ArrayList<>();

    public static Song getSongByName(String songName) {
        return new Song(Convert.getPathFromTitle(songName));
    }

    public static ArrayList<String> getDistinctAuthors() {
        ArrayList<String> result = new ArrayList<>();

        for (String name : authors) {
            if (!result.contains(name))
                result.add(name);
        }

        return result;
    }

    public static ArrayList<String> getArtistSongs(String name) {
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i).equals(name))
                result.add(songs.get(i));
        }

        return result;
    }

    public static String getCurrentCover() {
        String currentTrackTitle = Globs.currentSongs.get(Globs.currentTrackNumber).getTitle();

        return covers.get(getSongNumber(currentTrackTitle));
    }

    public static void addCover(String url) {
        covers.add(url);
    }

    public static void addArtist(String artistName) {
        authors.add(artistName);
    }

    public static void checkCoversArtists(Activity act) {
        for (String trackPath : songs) {
            try {
                JSONObject obj = new JSONObject(readJSONFromAsset(act));
                JSONArray jsonData = obj.getJSONArray("tracks");

                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject jsonObject = jsonData.getJSONObject(i);

                    String title = jsonObject.getString("title");
                    String coverPath = jsonObject.getString("cover");
                    String artist = jsonObject.getString("artist");

                    if (trackPath.equals(title)) {
                        addCover(DataLoader.BASE_URL + coverPath);
                        addArtist(artist);
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String readJSONFromAsset(@NonNull Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("relations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    public static int getSongNumber(String title) {
        int result = 0;
        String path = Convert.getPathFromTitle(title);

        for (int i = 0; i < songs.size(); i++) {
            String songPath = songs.get(i);
            if (!path.equals(songPath)) continue;

            result = i;
        }

        return result;
    }

}
