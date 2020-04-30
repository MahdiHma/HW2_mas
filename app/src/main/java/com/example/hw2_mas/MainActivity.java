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

public class MainActivity extends AppCompatActivity {
    private Switch swEnableLock;
    private Button btnLockScreen;
    static final int RESULT_ENABLE = 1;
    DevicePolicyManager deviceManger;
    ComponentName compName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swEnableLock = findViewById(R.id.sw_enable_lock);
        btnLockScreen = findViewById(R.id.btn_lock);

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
        Log.i("main Activity","done");

//        Intent intent = new Intent(this, FlatDetectorService.class);
//        startService(intent);
    }


    public void lockPhone(View view) {
        System.out.println("dddddddddddddddddddd");
        Intent intent = new Intent(this, FlatDetectorService.class);
        startService(intent);
//        deviceManger.lockNow();
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
