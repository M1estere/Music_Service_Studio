package com.example.music_service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.music_service.models.globals.Globs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountActivityViewModel extends BaseObservable {

    private String currentUserName;
    private String currentUserUsername;

    @Bindable
    public String getCurrentUserName() {
        return currentUserName;
    }
    @Bindable
    public String getCurrentUserUsername() {
        return currentUserUsername;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;

        notifyPropertyChanged(BR.currentUserName);
    }
    public void setCurrentUserUsername(String currentUserUsername) {
        this.currentUserUsername = currentUserUsername;

        notifyPropertyChanged(BR.currentUserUsername);
    }

    private Activity activity;

    private FirebaseFirestore firestore;
    private CollectionReference reference;
    public AccountActivityViewModel(Activity act) {
        this.activity = act;

        firestore = FirebaseFirestore.getInstance();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference = firestore.collection(userId);

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

    public void logOut() {
        Globs.recheckLogin = false;

        Intent intent = new Intent(activity, AuthenticationActivity.class);
        activity.startActivity(intent);
    }

    public void back() {
        activity.onBackPressed();
    }
}
