package com.hacks.societyapp.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SwipeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt("com.my.app.notificationId");
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        SharedPreferences prefs = context.getSharedPreferences("notifs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        if (notificationManager != null) {
            edit.putInt(String.valueOf(notificationId), 1);
            edit.commit();
            notificationManager.cancel(notificationId);
        }
    }
}
