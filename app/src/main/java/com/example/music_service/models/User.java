package com.example.music_service.models;

import android.graphics.Bitmap;

public class User {
    private static String name;
    private static String userName;

    private static Bitmap bitmap;

    public static String getName() {
        return name;
    }

    public static void setName(String n) {
        name = n;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String user) {
        userName = user;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bit) {
        bitmap = bit;
    }
}
