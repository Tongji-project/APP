package com.tongjiapp.remirobert.tongji_localisation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by remirobert on 02/05/16.
 */

interface DeviceBatteryManagerListener {
    void onReceiveBatteryLevel(int level);
}

public class DeviceBatteryManager {

    private Context mContext;
    private BroadcastReceiver mBroadcastReceiver;

    public DeviceBatteryManager(Context context) {
        mContext = context;
    }

    public void getBatteryLevel(final DeviceBatteryManagerListener listener) {

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                if (listener != null) {
                    listener.onReceiveBatteryLevel(level);
                }
                mContext.unregisterReceiver(mBroadcastReceiver);
            }
        };
        mContext.registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
}
