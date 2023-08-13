package com.allrecipes.recipes5;

/**
 * Created by Mukesh Yadav on 10/28/2018.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;


public class TimerExpiredReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, Just_base.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        b.setSound(notification)
                .setContentTitle("finished")
                .setAutoCancel(true)
                .setContentText("finished")
                .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                .setContentIntent(pIntent);

        Notification n = b.build();
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, n);
    }
}