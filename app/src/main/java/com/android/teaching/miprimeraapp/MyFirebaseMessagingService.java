package com.android.teaching.miprimeraapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        Log.d("MyFirebaseMessaging", "Data receiver: " + data.toString());

        if (data.containsKey("show_notification")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationForOreo();
            } else {
                createNotificationForLowerThanOreo();
            }
        }
    }

    private void createNotificationForLowerThanOreo() {
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("HOLA CLASE")
                .setContentText("Hoy estáis de lunes (y yo también)");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());
    }

    @TargetApi(26)
    private void createNotificationForOreo() {
        NotificationChannel channel = new NotificationChannel("ID", "name", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Notification.Builder builder = new Notification
                .Builder(this, "ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("HOLA CLASE")
                .setContentText("Hoy estáis de lunes (y yo también)");

        notificationManager.notify(1, builder.build());
    }
}








