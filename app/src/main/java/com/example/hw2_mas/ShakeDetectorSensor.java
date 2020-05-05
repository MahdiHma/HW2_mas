package com.example.hw2_mas;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class ShakeDetectorSensor implements SensorEventListener {
    private Vibrator vibrator;
    private SensorManager shakeManager;
    private Context context;
    private Sensor shakeSensor;
    private float currentAcceleration;
    private float lastAcceleration;
    public static float TRESHLD_DEFALUT = 5f;
    private float threshold;

    public ShakeDetectorSensor(Context context) {
        this.context = context;
        shakeManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        shakeSensor = shakeManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        lastAcceleration = currentAcceleration;
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        currentAcceleration = (float) Math.sqrt(x * x + y * y + z * z);
        if (currentAcceleration - lastAcceleration > threshold) {
            Log.i("TAG", "onSensorChanged:tresh "+threshold);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(400);
            }
            unlockPhone();
            Toast.makeText(context, accelerationReport(), Toast.LENGTH_LONG).show();

        }

    }

    private String accelerationReport() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("phone shake by ");
        stringBuilder.append(currentAcceleration - lastAcceleration);
        stringBuilder.append(" m/s^2 acceleration");
        return stringBuilder.toString();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void stopListening() {
        shakeManager.unregisterListener(this);
    }

    public void startListening() {
        shakeManager.registerListener(this, shakeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unlockPhone() {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        assert powerManager != null;
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,"appname::WakeLock");
        wakeLock.acquire();
        wakeLock.release();
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
}
