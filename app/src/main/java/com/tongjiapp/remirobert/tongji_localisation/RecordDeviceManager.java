package com.tongjiapp.remirobert.tongji_localisation;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by remirobert on 02/05/16.
 */

interface RecordDeviceManagerListener {
    void onRecordDeviceInformations(Record record);
}

public class RecordDeviceManager {

    private Context mContext;

    private DeviceTelephonyManager mDeviceTelephonyManager;
    private DeviceLocationManager mDeviceLocationManager;
    private RecordApiManager mRecordApiManager;

    private Record mRecord;
    private RecordDeviceManagerListener mRecordDeviceManagerListener;

    private Realm mRealm;

    private void initRealmInstance() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mContext).build();
        Realm realm = Realm.getInstance(realmConfig);
    }

    public RecordDeviceManager(Context context) {
        mContext = context;
        mDeviceTelephonyManager = new DeviceTelephonyManager(mContext);
        mDeviceLocationManager = new DeviceLocationManager(mContext);
        mRecordApiManager = new RecordApiManager(mContext);
        initRealmInstance();
    }

    private void fetchLocation(final String provider) {
        mDeviceLocationManager.getLocation(provider, new DeviceLocationManagerListener() {
            @Override
            public void onReveiceLocation(DLocation location) {
                if (provider == LocationManager.NETWORK_PROVIDER) {
                    mRecord.setNetworkLocation(location);
                    fetchLocation(LocationManager.GPS_PROVIDER);
                }
                else {
                    mRecord.setGpsLocation(location);
                    mRecordDeviceManagerListener.onRecordDeviceInformations(mRecord);
                }
            }
        });
    }

    private void saveRecord(RecordDevice recordDevice) {

    }

    private void saveRecordDeviceInformation(Device device, RecordDevice recordDevice) {
//        RealmQuery<Device> query = mRealm.where(Device.class);

//        Device deviceRegistred = query.findFirst();
//        if (deviceRegistred == null) {
//            mRealm.beginTransaction();
//            mRealm.copyToRealm(device);
//            mRealm.commitTransaction();
//            //Todo send data request here to the server
//        }

        String jsonDevice = device.toString();

        mRecordApiManager.createNewDevice(device, new RecordApiManagerListener() {
            @Override
            public void onReceiveReponse(RecordApiManagerStatus status) {
                Log.v("OK", "status : " + status);
            }
        });
    }

    public void newRecord(RecordDeviceManagerListener listener) {
        mRecord = new Record();
        mRecordDeviceManagerListener = listener;

        mDeviceTelephonyManager.getGsmInformation(new DeviceTelephonyManagerListener() {
            @Override
            public void onReceiveDevice(Device device, RecordDevice recordDevice) {
                mRecord.setDevice(recordDevice);
//                saveRecordDeviceInformation(device, recordDevice);
                fetchLocation(LocationManager.NETWORK_PROVIDER);
            }
        });
    }
}
