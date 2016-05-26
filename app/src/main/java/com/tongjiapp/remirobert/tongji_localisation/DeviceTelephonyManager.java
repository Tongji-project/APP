package com.tongjiapp.remirobert.tongji_localisation;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

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

        String networkOperator = telephonyManager.getNetworkOperator();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mContext).build();
        Realm realm = Realm.getInstance(realmConfig);

        Device localDevice;
        String deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        RealmResults<Device> devices = realm.where(Device.class).findAll();

        localDevice = devices.where().equalTo("mDeviceId", deviceId).findFirst();
        Log.v(TAG, "find device for device id  : " + deviceId);
        if (localDevice == null) {
            Log.v(TAG, "local device null creation");
            localDevice = new Device();
        }

        Log.v(TAG, "local device = " + localDevice);

        realm.beginTransaction();
        final Device device = localDevice;
        localDevice.setDeviceId(deviceId);

//        final Device device = new Device();
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

        realm.commitTransaction();

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
