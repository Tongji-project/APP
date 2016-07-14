package com.tongjiapp.remirobert.tongji_localisation;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by remirobert on 02/05/16.
 */

interface RecordDeviceManagerListener {
    void onRecordDeviceInformations(Record record, Device device);
}

public class RecordDeviceManager {

    private static final String TAG = "RecordDeviceManager";

    private Context mContext;

    private DeviceTelephonyManager mDeviceTelephonyManager;
    private DeviceLocationManager mDeviceLocationManager;
    private RecordApiManager mRecordApiManager;

    private Record mRecord;
    private Device mDevice;
    private RecordDeviceManagerListener mRecordDeviceManagerListener;

    private Realm mRealm;

    private void initRealmInstance() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mContext).build();
        mRealm = Realm.getInstance(realmConfig);
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
                    mRecordDeviceManagerListener.onRecordDeviceInformations(mRecord, mDevice);
                    saveRecordDeviceInformation(mDevice, mRecord.getDevice());
                }
            }
        });
    }

    private void saveRecord() {
        mRecordApiManager.createNewRecord(mRecord, mDevice.getDeviceId(), new RecordApiManagerListener() {
            @Override
            public void onReceiveReponse(RecordApiManagerStatus status) {
                if (status == RecordApiManagerStatus.SUCCESS) {
                    Log.v(TAG, "save new record success");
                    Toast.makeText(mContext , "Success upload record",
                                    Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(mContext , "Error upload record",
                                    Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveOrUpdateDevice(final Device device) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(device);
                saveRecord();
            }
        });
    }

    private void saveRecordDeviceInformation(final Device device, final RecordDevice recordDevice) {
        RealmResults<Device> devices = mRealm.where(Device.class).findAll();
        final Device localDevice = devices.where().equalTo("mDeviceId", device.getDeviceId()).findFirst();

        if (localDevice == null || !localDevice.isUploaded()) {
            mRecordApiManager.createNewDevice(device, new RecordApiManagerListener() {
                @Override
                public void onReceiveReponse(RecordApiManagerStatus status) {
                    if (status == RecordApiManagerStatus.SUCCESS) {
                        if (localDevice != null) {
                            mRealm.beginTransaction();
                            localDevice.setUploaded(true);
                            mRealm.commitTransaction();
                            saveOrUpdateDevice(localDevice);
                        }
                        else {
                            mRealm.beginTransaction();
                            device.setUploaded(true);
                            mRealm.commitTransaction();
                            saveOrUpdateDevice(device);
                        }
                    }
                }
            });
        }
        else {
            saveOrUpdateDevice(device);
        }
    }

    public void newRecord(RecordDeviceManagerListener listener) {
        mRecord = new Record();
        mRecordDeviceManagerListener = listener;

        mDeviceTelephonyManager.getGsmInformation(new DeviceTelephonyManagerListener() {
            @Override
            public void onReceiveDevice(Device device, RecordDevice recordDevice) {
                mRecord.setDevice(recordDevice);
                mDevice = device;
                fetchLocation(LocationManager.NETWORK_PROVIDER);
            }
        });
    }
}
