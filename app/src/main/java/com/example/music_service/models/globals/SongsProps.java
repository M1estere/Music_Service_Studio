package com.example.music_service.models.globals;

import android.app.Activity;
import android.widget.Toast;

import com.example.music_service.models.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SongsProps {

    public static ArrayList<String> songs = new ArrayList<>();
    public static ArrayList<String> authors = new ArrayList<>();
    public static ArrayList<String> uris = new ArrayList<>();
    public static ArrayList<String> covers = new ArrayList<>();

    public static Song getSongByName(String songName) {
        return new Song(songName);
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
                        addCover(Globs.BASE_URL + coverPath);
                        addArtist(artist);
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String readJSONFromAsset(Activity activity) {
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

}
