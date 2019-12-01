package com.kaminski.gothan.util;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class Alert {

    public static void showAlert(Context c, String title, String message, String stringButtonNeutral){

        AlertDialog.Builder msg = new AlertDialog.Builder(c);
        msg.setTitle(title);
        msg.setMessage(message);
        msg.setNeutralButton(stringButtonNeutral, null);
        msg.show();

    }
}
