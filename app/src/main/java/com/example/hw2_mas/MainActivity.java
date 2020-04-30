package com.example.hw2_mas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Switch swEnableLock;
    private Switch swLockService;
    static final int RESULT_ENABLE = 1;
    DevicePolicyManager deviceManger;
    ComponentName compName;
    private Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swEnableLock = findViewById(R.id.sw_enable_lock_access);
        swLockService = findViewById(R.id.sw_enable_lock_service);
        serviceIntent = new Intent(this, FlatDetectorService.class);
        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this, DeviceAdmin.class);
        boolean active = deviceManger.isAdminActive(compName);
        swEnableLock.setChecked(active);
        if (active) {
            swLockService.setVisibility(View.VISIBLE);
        } else {
            swLockService.setVisibility(View.GONE);
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
                } else if (active) {
                    deviceManger.removeActiveAdmin(compName);
                    swLockService.setChecked(false);
                    swLockService.setVisibility(View.GONE);
                    stopService(serviceIntent);
                }
            }
        });
        swLockService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("sw_lock","changed"+isChecked);
                if (isChecked) {
                    startService(serviceIntent);
                }
                else{
                    stopService(serviceIntent);
                }
            }
        });
        Log.i("main Activity", "done");

//        Intent intent = new Intent(this, FlatDetectorService.class);
//        startService(intent);
    }

//
//    public void lockPhone(View view) {
//        Intent intent = new Intent(this, FlatDetectorService.class);
//        startService(intent);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    swLockService.setVisibility(View.VISIBLE);
                } else {
                    swEnableLock.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Failed!",
                            Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

}
