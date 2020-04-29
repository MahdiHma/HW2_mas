package com.example.hw2_mas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Switch swEnableLock;
    private Button btnLockScreen;
    static final int RESULT_ENABLE = 1;
    DevicePolicyManager deviceManger;
    ComponentName compName;
    private SensorManager sensorManager;
    private Sensor gravitySensor;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float norm_Of_g =(float) Math.sqrt(x * x + y * y + z * z);
//
//        x = (x / norm_Of_g);
//        y = (y / norm_Of_g);
        z = (z / norm_Of_g);
        int inclination = (int) Math.round(Math.toDegrees(Math.acos(z)));
        Log.i("tag","incline is:"+inclination);
        if (inclination < 25 || inclination > 155)
        {
            lockPhone();
            Toast.makeText(this,"device flat - beep!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swEnableLock = findViewById(R.id.sw_enable_lock);
        btnLockScreen = findViewById(R.id.btn_lock);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if(gravitySensor == null){
            //todo handle
        }

        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this, DeviceAdmin.class);
        boolean active = deviceManger.isAdminActive(compName);
        swEnableLock.setChecked(active);
        if (active) {
            btnLockScreen.setVisibility(View.VISIBLE);
        } else {
            btnLockScreen.setVisibility(View.GONE);
        }

        swEnableLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean active = deviceManger.isAdminActive(compName);
                if (isChecked && !active) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
                    startActivityForResult(intent, RESULT_ENABLE);
                } else if(active){
                    deviceManger.removeActiveAdmin(compName);
                    btnLockScreen.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void lockPhone() {
        deviceManger.lockNow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    btnLockScreen.setVisibility(View.VISIBLE);
                } else {
                    swEnableLock.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Failed!",
                            Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

}
