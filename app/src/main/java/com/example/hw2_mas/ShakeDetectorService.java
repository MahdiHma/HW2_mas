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
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        return Service.START_STICKY;
    }
    @Override
    public void onDestroy(){

    }
    @Override
    public void onCreate(){
        Sensor accelerometer;
        Context context = getBaseContext();
        SensorManager shakeManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (shakeManager != null) {
            accelerometer = shakeManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            Toast.makeText(context, "accelerometer not found", Toast.LENGTH_SHORT).show();
        }


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
