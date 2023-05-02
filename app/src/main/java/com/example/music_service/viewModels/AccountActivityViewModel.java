package com.example.music_service.viewModels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.music_service.BR;
import com.example.music_service.models.CreateNotification;
import com.example.music_service.models.Player;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.views.AuthenticationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountActivityViewModel extends BaseObservable {

    private final Activity activity;
    private String currentUserName;
    private String currentUserUsername;

    public AccountActivityViewModel(Activity act) {
        this.activity = act;

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

                        setCurrentUserName(name);
                        setCurrentUserUsername(nickname);
                    }
                }
                progressDialog.dismiss();
            }
        });
    }

    @Bindable
    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;

        notifyPropertyChanged(BR.currentUserName);
    }

    @Bindable
    public String getCurrentUserUsername() {
        return currentUserUsername;
    }

    public void setCurrentUserUsername(String currentUserUsername) {
        this.currentUserUsername = currentUserUsername;

        notifyPropertyChanged(BR.currentUserUsername);
    }

    public void logOut() {
        Globs.recheckLogin = false;
        CreateNotification.destroyNotification();
        FirebaseAuth.getInstance().signOut();
        Player.drop();

        Intent intent = new Intent(activity, AuthenticationActivity.class);
        activity.startActivity(intent);
    }

    public void back() {
        activity.onBackPressed();
    }
}
