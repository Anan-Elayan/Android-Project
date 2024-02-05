package com.example.androidproject.Task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.androidproject.R;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create and show the notification
        showNotification(context, "Your Notification Title", "Your Notification Content");
    }

    private void showNotification(Context context, String title, String content) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
//                .setSmallIcon(R.drawable.ic_notification) // Replace with your app's notification icon
//                .setContentTitle(title)
//                .setContentText(content)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(1, builder.build());
    }
}
