package com.example.music_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.music_service.activities.MainActivity;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.SongsProps;

public class TracksLoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_load);

        loadTracksFromFirebaseStorage();
    }

    private void loadTracksFromFirebaseStorage() {
        /*ProgressBar progressBar = findViewById(R.id.progress_bar);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("music");
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                List<StorageReference> result = listResult.getItems();
                for (int i = 0; i < result.size(); i++) {
                    StorageReference item = result.get(i);
                    String path = item.getName();
                    String artist = "Temporary";

                    if (i == result.size() - 1) {
                        item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String uriString = uri.toString();

                                addSong(uriString, path, artist);
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                progressBar.animate().alpha(0).setDuration(100);

                                Collections.sort(SongsProps.songs);
                                openMainPage();
                            }
                        });

                        continue;
                    }

                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String uriString = uri.toString();

                            addSong(uriString, path, artist);
                        }
                    });
                }
            }
        });*/

        Globs.fillSongsFromDrive(this);
        openMainPage();
    }

    public static void addSong(String uri, String title) {
        SongsProps.uris.add(Globs.BASE_URL + uri);
        SongsProps.songs.add(title);
    }

    private void openMainPage() {
        Globs.recheckLogin = false;

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}