package com.example.music_service.models.data;

import android.app.Activity;

import com.example.music_service.models.data.SongsProps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataLoader {

    public static final String BASE_URL = "https://drive.google.com/uc?id=";

    public static void fillSongsFromDrive(Activity activity) {
        try {
            JSONObject obj = new JSONObject(SongsProps.readJSONFromAsset(activity));
            JSONArray jsonData = obj.getJSONArray("tracks");

            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject jsonObject = jsonData.getJSONObject(i);

                String path = jsonObject.getString("path");
                String title = jsonObject.getString("title");

                addSong(path, title);
            }

            System.out.printf("Result amount uris: %d\n", SongsProps.uris.size());
            System.out.printf("Result amount songs: %d\n", SongsProps.songs.size());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        SongsProps.checkCoversArtists(activity);
    }

    public static void addSong(String uri, String title) {
        SongsProps.uris.add(BASE_URL + uri);
        SongsProps.songs.add(title);
    }

}
