package com.example.hw2_mas;

import android.app.IntentService;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class FlatDetectorService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private int lockAngle;
    private DevicePolicyManager deviceManger;
    public void onCreate() {
        Log.i("service ", "started");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (gravitySensor == null) {
            //todo handle
        }
    }

    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        lockAngle = intent.getIntExtra("lockAngle",25);
        Log.i("service", "start command started"+lockAngle);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        int i = 0;
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Log.i("service", "start command ended");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        Log.i("service ", "destroyed");
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float norm_Of_g = (float) Math.sqrt(x * x + y * y + z * z);
        z = (z / norm_Of_g);
        int inclination = (int) Math.round(Math.toDegrees(Math.acos(z)));
        Log.i("tag", "incline is:" + inclination);
        if (inclination < lockAngle || inclination > 180-lockAngle) {
            deviceManger.lockNow();
            Toast.makeText(this, "device flat - beep!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
