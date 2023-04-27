package com.example.music_service.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.music_service.R;
import com.example.music_service.viewModels.UserPlaylistsViewModel;
import com.example.music_service.viewModels.UserSongsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomPlaylists {

    private static ArrayList<String> playlistNames = new ArrayList<>();
    public static ArrayList<String> getPlaylistNames() {
        return playlistNames;
    }

    private static ArrayList<String> playlistContents = new ArrayList<>();
    public static ArrayList<String> getPlaylistContents() {
        return playlistContents;
    }

    private static UserPlaylistsViewModel userPlaylistsViewModel;
    public static void setUserPlaylistsViewModel(UserPlaylistsViewModel uvm) {
        userPlaylistsViewModel = uvm;
    }

    public static void savingPlaylist(Context context, ArrayList<String> titles) {
        showDialog(context, titles);
    }

    private static void showDialog(Context context, ArrayList<String> titles) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);

        View customLayout = ((Activity) context).getLayoutInflater().inflate(R.layout.playlist_name_dialog, null);

        EditText editText = customLayout.findViewById(R.id.name_edit_text);
        editText.setSelected(true);

        AlertDialog alertDialog = alertDialogBuilder
                .setView(customLayout)
                .setCancelable(true)
                .setTitle("Save playlist")
                .setPositiveButton("Save", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String title = editText.getText().toString();
                        if (playlistNames.contains(title)) {
                            editText.setText(null);
                            Toast.makeText(context, "Playlist " + title + " already exists", Toast.LENGTH_SHORT).show();
                        }

                        if (title.length() > 1) {
                            savePlaylist(context, title, titles);
                            alertDialog.dismiss();
                        } else {
                            editText.setText(null);
                            Toast.makeText(context, "Enter a valid name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    private static void savePlaylist(Context context, String name, @NonNull ArrayList<String> titles) {
        if (titles.size() < 1) return;

        String titlesString = String.join(" ", titles);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(context, "No user currently registered", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference reference = firestore.collection(user.getUid());

        Map<String, Object> userData = new HashMap<>();
        userData.put("songs", titlesString);
        userData.put("playlist_name", name);

        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentReference documentReference = reference.document(name + " playlist");
                    documentReference.set(userData);
                }
            }
        });

        Toast.makeText(context, "Saved under: " + name, Toast.LENGTH_SHORT).show();

        CustomPlaylists.loadPlaylists();
    }

    public static void loadPlaylists() {
        playlistNames.clear();
        playlistContents.clear();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return;

        CollectionReference reference = firestore.collection(user.getUid());

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                playlistNames.clear();
                playlistContents.clear();

                if (value != null && !value.getDocuments().isEmpty()) {
                    List<DocumentSnapshot> documents = value.getDocuments();
                    System.out.printf("Documents: %d\n", documents.size());
                    for (int i = 0; i < documents.size(); i++) {
                        if (documents.get(i).exists()) {
                            String name = documents.get(i).getString("playlist_name");
                            String songs = documents.get(i).getString("songs");

                            if (name != null && songs != null) {
                                playlistNames.add(documents.get(i).getString("playlist_name"));
                                playlistContents.add(documents.get(i).getString("songs"));
                            }
                        } else {
                            playlistNames.clear();
                            playlistContents.clear();
                        }
                    }
                    if (userPlaylistsViewModel != null) userPlaylistsViewModel.updatePlaylists();
                } else {
                    playlistNames.clear();
                    playlistContents.clear();
                }
            }
        });
    }

    public static ArrayList<String> fromStringsToPlaylist(String source) {
        source = source.trim();
        List<String> resultList = Arrays.asList(source.split(" "));

        return new ArrayList<>(resultList);
    }

}
