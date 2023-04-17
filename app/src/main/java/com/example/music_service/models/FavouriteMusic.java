package com.example.music_service.models;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.music_service.models.globals.Convert;
import com.example.music_service.viewModels.UserSongsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FavouriteMusic {
    private static String favouriteTitles = "";

    private static UserSongsViewModel userSongsViewModel;
    public static void setUserSongsViewModel(UserSongsViewModel uvm) {
        userSongsViewModel = uvm;
    }

    public static String getFavouriteTitles() {
        return favouriteTitles.trim();
    }

    public static void saveCollectionToFile(String title, Activity activity, boolean added) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(activity, "No user currently registered", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference reference = firestore.collection(user.getUid());

        Map<String, Object> userData = new HashMap<>();
        userData.put("favourite_titles", favouriteTitles);
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    documentSnapshot.getReference().update(userData);

                    if (added)
                        Toast.makeText(activity,  "(Favourites) " + title + " was added", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(activity, "(Favourites) " + title + " was deleted", Toast.LENGTH_SHORT).show();

                    if (userSongsViewModel != null) userSongsViewModel.updateSongs();
                }
            }
        });
    }

    public static void loadCollectionFromFile() {
        empty();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return;

        CollectionReference reference = firestore.collection(user.getUid());

        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                    favouriteTitles = documentSnapshot.getString("favourite_titles");

                    if (favouriteTitles == null) favouriteTitles = "";
                }
            }
        });
    }

    public static void empty() {
        favouriteTitles = "";
    }

    public static void addToFavourites(String title, Activity activity) {
        String path = Convert.getPathFromTitle(title);

        if (favouriteTitles.contains(path)) {
            removeFromFavourites(title, activity);
            return;
        }

        if (favouriteTitles.isEmpty() == false)
            favouriteTitles += String.format(" %s", path);
        else
            favouriteTitles += path;

        saveCollectionToFile(title, activity, true);
    }

    public static void removeFromFavourites(String title, Activity activity) {
        String path = Convert.getPathFromTitle(title);

        if (favouriteTitles.contains(path) == false) return;

        favouriteTitles = favouriteTitles.replace(path, "");

        saveCollectionToFile(title, activity, false);
    }

    public static boolean trackInFavourites(String title) {
        return favouriteTitles.contains(Convert.getPathFromTitle(title));
    }

    public static int size() {
        return getArrayTitles().size();
    }

    public static ArrayList<String> getArrayTitles() {
        ArrayList<String> proper = new ArrayList<>(Arrays.asList(favouriteTitles.trim().split(" ")));
        proper.removeAll(Arrays.asList("", null));

        return proper;
    }

}
