package com.example.music_service.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationActionService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("SONGS_SONGS")
                .putExtra("action_name", intent.getAction()));
    }
}
