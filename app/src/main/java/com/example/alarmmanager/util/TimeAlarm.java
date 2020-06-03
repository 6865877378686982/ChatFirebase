package com.example.alarmmanager.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.alarmmanager.R;
import com.example.alarmmanager.activity.ShowReminderMessage;


public class TimeAlarm extends BroadcastReceiver {

    private NotificationManager nm;
    private String event;
    private Notification noti;

    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle extras = intent.getExtras();
        event = extras.getString("event");
        Bitmap bigIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // when you click on the notification
        Intent intent2 = new Intent(context, ShowReminderMessage.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.putExtra("ReminderMsg", event);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 1, intent2, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        Intent intent1 = new Intent(context, TimeAlarm.class);
        intent1.putExtra("event", "eventEntered");
        intent1.putExtra("time", "timeEntered");
        intent1.putExtra("date", "dateEntered");
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);

        mBuilder.setSmallIcon(R.drawable.ic_alarm_white_24dp);
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);


      /*  NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.small_notify_icon)
                // .setLargeIcon(bigIcon)
                .setColor(46254)
                .setAutoCancel(true)
                .setContentTitle("Reminder")
                .setContentText(event)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());*/


    }
}

