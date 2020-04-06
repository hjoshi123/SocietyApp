package com.hacks.societyapp.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.hacks.societyapp.R;
import com.hacks.societyapp.service.SwipeBroadcastReceiver;

class NotificationUtils {
    private static final String SOC_NOTIFICATION_CHANNEL = "society";
    private static final String SOC_APP_NAME = "SocietyApp";
    private static final String GROUP_NAME = "com.hacks.societyapp.Notifications";

    static void reminderUserAboutOrderStatus(Context context, int orderNumber, String status,
                                             String orderDate, int orderValue) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        String content = "Your order number " + orderNumber + " with cart value " + orderValue
                +  " has been " + status;

        NotificationCompat.Builder notification =
            new NotificationCompat.Builder(context, SOC_NOTIFICATION_CHANNEL)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(largeIcon(context))
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentTitle("Order Status for order on " + orderDate)
            .setContentText(content)
            .setGroup(GROUP_NAME)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
            .setOngoing(false)
            .setDeleteIntent(createDismissPendingIntent(context, orderNumber))
            .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(SOC_NOTIFICATION_CHANNEL);
            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            NotificationChannel channel = new NotificationChannel(SOC_NOTIFICATION_CHANNEL,
                    SOC_APP_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(R.color.colorPrimary);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100L, 200L, 300L, 400L, 500L, 400L, 300L,
                    200L, 400L});
            channel.setSound(ringtoneUri, audioAttributes);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(orderNumber, notification.build());
        }
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
    }

    private static PendingIntent createDismissPendingIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, SwipeBroadcastReceiver.class);
        intent.putExtra("com.my.app.notificationId", notificationId);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }
}
