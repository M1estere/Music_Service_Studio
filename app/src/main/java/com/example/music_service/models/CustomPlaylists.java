package com.example.music_service.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.music_service.R;
import com.example.music_service.models.globals.Convert;
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
                .setNegativeButton("Cancel", null)
                .create();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (charSequence.length() < 1) {
                    button.setEnabled(false);
                    button.setTextColor(Color.parseColor("#6C6C6C"));
                } else {
                    button.setEnabled(true);
                    button.setTextColor(Color.parseColor("#FFFFFFFF"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setEnabled(false);
                button.setTextColor(Color.parseColor("#6C6C6C"));
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

    public static void deletePlaylist(String name) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return;

        CollectionReference reference = firestore.collection(user.getUid());
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentReference documentReference = reference.document(name + " playlist");
                    documentReference.delete();
                }
            }
        });
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

    public static int removeSongFromPlaylist(Context context, String playlistName, String songName) {
        if (!playlistNames.contains(playlistName)) return -1;

        String titles = playlistContents.get(findPlaylist(playlistName));
        ArrayList<String> titlesList = fromStringsToPlaylist(titles);

        String neededPath = Convert.getPathFromTitle(songName);
        int result = titlesList.indexOf(neededPath);
        titlesList.remove(neededPath);

        savePlaylist(context, playlistName, titlesList);

        return result;
    }

    public static void addSongToPlaylist(Context context, String playlistName, String songName) {
        if (!playlistNames.contains(playlistName)) return;

        String titles = playlistContents.get(findPlaylist(playlistName));
        ArrayList<String> titlesList = fromStringsToPlaylist(titles);

        String neededPath = Convert.getPathFromTitle(songName);
        titlesList.remove(neededPath);
        titlesList.add(neededPath);

        savePlaylist(context, playlistName, titlesList);
    }

    public static ArrayList<Playlist> getPlaylists() {
        ArrayList<Playlist> result = new ArrayList<>();

        for (int i = 0; i < playlistNames.size(); i++) {
            Playlist playlist = new Playlist(playlistNames.get(i));
            playlist.setSongTitles(fromStringsToPlaylist(playlistContents.get(i)));

            result.add(playlist);
        }

        return result;
    }

    public static boolean songInList(String playlistName, String songName) {
        songName = Convert.getPathFromTitle(songName);

        if (!playlistNames.contains(playlistName)) return false;

        int id = playlistNames.indexOf(playlistName);
        String titles = playlistContents.get(id);

        if (!titles.contains(songName)) return false;

        return true;
    }

    public static int findPlaylist(String name) {
        return playlistNames.indexOf(name);
    }

    public static ArrayList<String> fromStringsToPlaylist(String source) {
        source = source.trim();
        List<String> resultList = Arrays.asList(source.split(" "));

        return new ArrayList<>(resultList);
    }

}
