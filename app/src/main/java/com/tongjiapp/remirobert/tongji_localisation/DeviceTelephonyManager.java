package com.tongjiapp.remirobert.tongji_localisation;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by remirobert on 02/05/16.
 */

interface DeviceTelephonyManagerListener {
    void onReceiveDevice(Device device, RecordDevice recordDevice);
}

public class DeviceTelephonyManager {

    private static final String TAG = "DeviceTelephonyManager";
    private Context mContext;

    private Boolean isFetched = false;
    private DeviceBatteryManager mDeviceBatteryManager;
    private DeviceSignalStrengthManager mDeviceSignalStrengthManager;

    private static int getApiVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    private static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    public DeviceTelephonyManager(Context context) {
        mContext = context;
    }

    public void getGsmInformation(final DeviceTelephonyManagerListener listener) {
        GsmCellLocation cellLocation = new GsmCellLocation();
        final TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        isFetched = false;
        Log.v(TAG, "get informations telephony");

        String networkOperator = telephonyManager.getNetworkOperator();

        final Device device = new Device();
        final RecordDevice recordDevice = new RecordDevice();

        if (!TextUtils.isEmpty(networkOperator)) {
            int mcc = Integer.parseInt(networkOperator.substring(0, 3));
            int mnc = Integer.parseInt(networkOperator.substring(3));

            device.setMcc(mcc);
            device.setMnc(mnc);
        }
        recordDevice.setCountryIso(telephonyManager.getNetworkCountryIso());
        device.setOperatorName(telephonyManager.getNetworkOperatorName());
        recordDevice.setLac(cellLocation.getLac());
        recordDevice.setCid(cellLocation.getCid());
        device.setAndroidVersion(getApiVersion());
        device.setPhoneModel(getDeviceModel());

        mDeviceBatteryManager = new DeviceBatteryManager(mContext);

        mDeviceBatteryManager.getBatteryLevel(new DeviceBatteryManagerListener() {
            @Override
            public void onReceiveBatteryLevel(int level) {
                recordDevice.setBatteryLevel(level);

                mDeviceSignalStrengthManager = new DeviceSignalStrengthManager(telephonyManager);
                mDeviceSignalStrengthManager.getSignalStrength(new DeviceSignalStrengthManagerListener() {
                    @Override
                    public void onReceiveSignalStrength(int signalStrength, int signalStrengthPercent) {
                        recordDevice.setSignalStrength(signalStrength);
                        if (!isFetched) {
                            listener.onReceiveDevice(device, recordDevice);
                        }
                        isFetched = true;
                    }
                });
            }
        });
    }
}
