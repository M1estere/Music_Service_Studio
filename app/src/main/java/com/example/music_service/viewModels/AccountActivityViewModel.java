package com.example.music_service.viewModels;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BaseObservable;

import com.example.music_service.R;
import com.example.music_service.models.BooleanDialog;
import com.example.music_service.models.CreateNotification;
import com.example.music_service.models.Player;
import com.example.music_service.models.User;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.views.AuthenticationActivity;
import com.example.music_service.views.PersonalizationActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivityViewModel extends BaseObservable {

    private final Activity activity;
    private String currentUserName;
    private String currentUserUsername;

    private ImageView image;

    public AccountActivityViewModel(Activity act) {
        activity = act;

        TextView name = activity.findViewById(R.id.user_name);
        TextView nickName = activity.findViewById(R.id.user_nick);

        image = activity.findViewById(R.id.user_image);
        image.setImageBitmap(User.getBitmap());

        name.setText(User.getName());
        nickName.setText(User.getUserName());
    }


    public void logOut() {
        boolean logOut = BooleanDialog.ConfirmDialog(activity, "Warning",
                "Are you sure you want to log out?", "Cancel", "Confirm",
                logOutProcess(), empty());
    }

    private Runnable logOutProcess() {
        return new Runnable() {
            @Override
            public void run() {
                Globs.recheckLogin = false;
                Globs.resetAllStatic();
                CreateNotification.destroyNotification();
                FirebaseAuth.getInstance().signOut();
                Player.drop();

                Intent intent = new Intent(activity, AuthenticationActivity.class);
                activity.startActivity(intent);
            }
        };
    }

    private Runnable empty() {
        return new Runnable() {
            @Override
            public void run() {
                return;
            }
        };
    }

    public void personalize() {
        activity.startActivity(new Intent(activity, PersonalizationActivity.class));
    }

    public void back() {
        activity.onBackPressed();
    }
}
