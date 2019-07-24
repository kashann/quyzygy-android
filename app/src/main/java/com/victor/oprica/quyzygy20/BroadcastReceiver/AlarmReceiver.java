package com.victor.oprica.quyzygy20.BroadcastReceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.victor.oprica.quyzygy20.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager1  =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
                    notificationManager.createNotificationChannel(notificationChannel);
                    builder = new NotificationCompat.Builder(context.getApplicationContext(), notificationChannel.getId());
                } else {
                   builder = new NotificationCompat.Builder(context.getApplicationContext());
                }

                 builder = builder.setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setContentTitle("Quyzygy Quiz App!")
                .setContentText("Hey! Time to learn something new today")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000,1000,1000,1000,1000});

        notificationManager.notify(0,builder.build());




    }
}
