package com.example.music_service.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;

import com.example.music_service.R;

public class BooleanDialog {

    public static Runnable ans_true = null;
    public static Runnable ans_false = null;

    public static boolean ConfirmDialog(Activity act, String Title, String ConfirmText,
                                        String CancelBtn, String OkBtn, Runnable trueProc, Runnable falseProc) {
        ans_true = trueProc;
        ans_false = falseProc;

        AlertDialog dialog = new AlertDialog.Builder(act, R.style.MyDialogTheme).create();
        dialog.setTitle(Title);
        dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + ConfirmText + "</font"));
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, OkBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ans_true.run();
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, CancelBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ans_false.run();
                        dialog.dismiss();
                    }
                });
        dialog.show();

        return true;
    }

}
