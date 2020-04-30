package com.example.hw2_mas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    private Switch swEnableLock;
    private Switch swLockService;
    private LinearLayout llLockService;
    private TextView etAngle;
    static final int RESULT_ENABLE = 1;
    DevicePolicyManager deviceManger;
    ComponentName compName;
    private Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("created","act");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swEnableLock = findViewById(R.id.sw_enable_lock_access);
        swLockService = findViewById(R.id.sw_enable_lock_service);
        llLockService = findViewById(R.id.ll_lockService);
        etAngle = findViewById(R.id.et_angle);
        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        serviceIntent = new Intent(this, FlatDetectorService.class);
        compName = new ComponentName(this, DeviceAdmin.class);
        boolean active = deviceManger.isAdminActive(compName);
        swEnableLock.setChecked(active);
        if (active) {
            llLockService.setVisibility(View.VISIBLE);
        } else {
            llLockService.setVisibility(View.GONE);
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
                    stopService(serviceIntent);
                    deviceManger.removeActiveAdmin(compName);
                    swLockService.setChecked(false);
                    llLockService.setVisibility(View.GONE);
                }
            }
        });
        swLockService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("sw_lock","changed"+isChecked);
                if (isChecked) {
                    if(!TextUtils.isEmpty(etAngle.getText())){
                        int lockAngle = Integer.parseInt(etAngle.getText().toString());
                        serviceIntent.putExtra("lockAngle",lockAngle);
                    }
                    startService(serviceIntent);
                }
                else{
                    stopService(serviceIntent);
                }
            }
        });
        Log.i("main Activity", "done");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    llLockService.setVisibility(View.VISIBLE);
                } else {
                    swEnableLock.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Failed!",
                            Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

}
