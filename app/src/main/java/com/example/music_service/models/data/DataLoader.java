package com.example.music_service.models.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.music_service.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataLoader {

    public static final String BASE_URL = "https://drive.google.com/uc?id=";

    public static void fillSongsFromDrive(Activity activity) {
        SongsProps.uris.clear();
        SongsProps.songs.clear();

        try {
            JSONObject obj = new JSONObject(SongsProps.readJSONFromAsset(activity));
            JSONArray jsonData = obj.getJSONArray("tracks");

            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject jsonObject = jsonData.getJSONObject(i);

                String path = jsonObject.getString("path");
                String title = jsonObject.getString("title");

                addSong(path, title);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        SongsProps.checkCoversArtists(activity);
    }

    public static void addSong(String uri, String title) {
        SongsProps.uris.add(BASE_URL + uri);
        SongsProps.songs.add(title);
    }

    public static void loadCurrentUserInfo(Activity activity) {
        loadImage(activity);
        loadInfo(activity);
    }

    private static void loadInfo(Activity activity) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference reference = firestore.collection(userId);

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Getting data...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.getString("name");
                        String nickname = document.getString("username");

                        User.setName(name);
                        User.setUserName(nickname);
                    }
                }
                progressDialog.dismiss();
            }
        });
    }

    private static void loadImage(Activity activity) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageReference = storageReference.child("users_images/" + userId + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024 *5;

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Getting data...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        imageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                User.setBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                User.setBitmap(null);
                progressDialog.dismiss();
                activity.onBackPressed();
            }
        });
    }

}
