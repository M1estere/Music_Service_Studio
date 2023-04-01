package com.example.music_service;

import android.app.Activity;

import androidx.databinding.BaseObservable;

public class AccountActivityViewModel extends BaseObservable {

    private Activity activity;

    public AccountActivityViewModel(Activity activity) {
        this.activity = activity;
    }

    public void back() {
        activity.onBackPressed();
    }
}
