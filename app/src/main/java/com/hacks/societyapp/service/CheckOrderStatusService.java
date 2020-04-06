package com.hacks.societyapp.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.hacks.societyapp.Utils.CheckOrderStatusWorker;

import java.util.concurrent.TimeUnit;

public class CheckOrderStatusService extends IntentService {
    public CheckOrderStatusService() {
        super("SocietyApp");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        PeriodicWorkRequest periodicRequest = new PeriodicWorkRequest.Builder(CheckOrderStatusWorker.class,
                20, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(periodicRequest);
    }
}
