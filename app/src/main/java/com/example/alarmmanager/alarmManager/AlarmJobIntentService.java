package com.example.alarmmanager.alarmManager;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class AlarmJobIntentService extends JobIntentService {
    String TAG = "AlarmJobIntentService";

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if(intent!=null){
            Log.d(TAG,"Intent action:: "+intent.getAction());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}
