package com.example.hw2_mas;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

public class ShakeDetectorSensor implements SensorEventListener {
    private Vibrator vibrator;
    private SensorManager shakeManager;
    private Context context;
    private Sensor shakeSensor;
    private int TRESHOLD;
    private float currentAcceleration;
    private float lastAcceleration;


    public ShakeDetectorSensor (Context context){
        this.context = context;
        shakeManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        shakeSensor = shakeManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void stopListening(){
        shakeManager.unregisterListener(this);
    }

    public void startListening(){
        shakeManager.registerListener(this, shakeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
