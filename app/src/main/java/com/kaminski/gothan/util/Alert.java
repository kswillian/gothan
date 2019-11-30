package com.kaminski.gothan.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class Alert {

    public static void showAlert(Context c, String title, String message, String stringButtonNeutral){

        AlertDialog.Builder msg = new AlertDialog.Builder(c);
        msg.setTitle(title);
        msg.setMessage(message);
        msg.setNeutralButton(stringButtonNeutral, null);
        msg.show();

    }

    public static void showAlert(Context c, String title, String message, String stringButtonNeutral, DialogInterface.OnClickListener listener){

        AlertDialog.Builder msg = new AlertDialog.Builder(c);
        msg.setTitle(title);
        msg.setMessage(message);
        msg.setNeutralButton(stringButtonNeutral, listener);
        msg.show();

    }
}
