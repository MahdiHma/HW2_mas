package com.example.hw2_mas;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Objects;

public class ShakeDetectorService extends Service{
    private ShakeDetectorSensor shakeDetectorSensor;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        shakeDetectorSensor.startListening();
        return Service.START_NOT_STICKY;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        shakeDetectorSensor.stopListening();
    }
    @Override
    public void onCreate(){
        super.onCreate();
        shakeDetectorSensor = new ShakeDetectorSensor(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
