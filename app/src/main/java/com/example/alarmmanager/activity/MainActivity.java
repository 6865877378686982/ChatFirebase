package com.example.alarmmanager.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarmmanager.R;
import com.example.alarmmanager.adapter.ListingsAdapter;
import com.example.alarmmanager.model.EventModelDB;
import com.example.alarmmanager.model.ListingsModel;
import com.example.alarmmanager.util.TimeAlarm;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    Button button;
    private ArrayList<ListingsModel> list;
    private RecyclerView rvListings;
    private ListingsAdapter adapter;
    private ListingsModel model;
    private LinearLayoutManager mLayoutManager;
    private Realm myRealm;
    private RealmResults<EventModelDB> results1;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_listing);

        rvListings = (RecyclerView) findViewById(R.id.events);

        button = findViewById(R.id.btn_shownotificattion);
        list = new ArrayList<ListingsModel>();
        Realm.init(getApplicationContext());
        // read the saved values in database
        myRealm = Realm.getDefaultInstance();
        results1 = myRealm.where(EventModelDB.class).findAll();

        for (int i = results1.size() - 1; i >= 0; i--) {
            EventModelDB c = results1.get(i);
            model = new ListingsModel();
            model.setEvent(c.getEvent());
            model.setTime(c.getTime());
            model.setDate(c.getDate());
            model.setTimestamp(c.getTimestamp());

            list.add(model);
        }


        adapter = new ListingsAdapter(list, getApplicationContext(), MainActivity.this);
        rvListings.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(this);
        //   mLayoutManager.setReverseLayout(true);
        //  mLayoutManager.setStackFromEnd(true);
        rvListings.setLayoutManager(mLayoutManager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");

                contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
                contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
                Intent intent = new Intent(getApplicationContext(), TimeAlarm.class);

                intent.putExtra("event", "eventEntered");
                intent.putExtra("time", "timeEntered");
                intent.putExtra("date", "dateEntered");
                PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);

                mBuilder.setSmallIcon(R.drawable.ic_alarm_white_24dp);
                mBuilder.setAutoCancel(false);
                mBuilder.setOngoing(true);
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                mBuilder.setOnlyAlertOnce(true);
                mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
                mBuilder.setContent(contentView);

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

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add) {
            startActivity(new Intent(getBaseContext(), AddEvent.class));
            return true;
        } else if (id == R.id.info) {
            Snackbar.make(findViewById(android.R.id.content), "Long Press an event to delete it", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.WHITE)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
