package com.example.hw2_mas;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;




public class FlatDetectorService extends IntentService implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gravitySensor;


    public FlatDetectorService() {
        super("FlatDetectorService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i("service ","started");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (gravitySensor == null) {
            //todo handle
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("service", "handle intent started");
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        int i = 0;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("service","handle intent ended");
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
        if (inclination < 25 || inclination > 155) {
//            lockPhone();
            Toast.makeText(this, "device flat - beep!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        Log.i("service ", "destroyed");
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
