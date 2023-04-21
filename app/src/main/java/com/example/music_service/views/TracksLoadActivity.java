package com.example.music_service.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.music_service.R;
import com.example.music_service.models.data.DataLoader;
import com.example.music_service.models.globals.Globs;

public class TracksLoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_load);

        loadTracksFromFirebaseStorage();
    }

    private void loadTracksFromFirebaseStorage() {
        /*
        //Load data from firebase
        ProgressBar progressBar = findViewById(R.id.progress_bar);

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

        DataLoader.fillSongsFromDrive(this);
        openMainPage();
    }

    private void openMainPage() {
        Globs.recheckLogin = false;

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}