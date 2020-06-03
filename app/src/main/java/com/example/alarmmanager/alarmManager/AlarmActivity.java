package com.example.alarmmanager.alarmManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmmanager.R;

public class AlarmActivity extends AppCompatActivity {

    String TAG = "AlarmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();
        if(intent!=null) {
            Log.d(TAG, "Action:: " + intent.getAction());
        }

    }
}
