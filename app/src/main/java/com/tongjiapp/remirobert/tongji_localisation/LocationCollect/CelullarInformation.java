package com.tongjiapp.remirobert.tongji_localisation.LocationCollect;

import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

/**
 * Created by remirobert on 19/04/16.
 */
public class CelullarInformation {

    private int mCid;
    private int mLac;
    private String mMcc;
    private String mMnc;
    private String mImsi;
    private String mDeviceId;
    private String mPhoneModel;
    private String mAndroidVersion;
    private Long mTimeStamp;
    private float mSignalStrength;
    private float mBatteryLevel;

    public CelullarInformation(TelephonyManager telephonyManager) {
        GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();

        mTimeStamp = System.currentTimeMillis() / 1000;
        mCid = cellLocation.getCid();
        mLac = cellLocation.getLac();

        mDeviceId = telephonyManager.getDeviceId();
        mImsi = telephonyManager.getSubscriberId();
        mPhoneModel = android.os.Build.MODEL;
        mAndroidVersion  = android.os.Build.VERSION.RELEASE;
    }

    public void setSignalStrength(float signalStrength) {
        this.mSignalStrength = signalStrength;
    }

    public void setBatteryLevel(float batteryLevel) {
        mBatteryLevel = batteryLevel;
    }

    /* Debug method */

    private static final String TAG = "CelullarInformation";

    public void description() {
        Log.v(TAG, "android version : " + mAndroidVersion);
        Log.v(TAG, "device Id : " + mDeviceId);
        Log.v(TAG, "CID : " + mCid);
        Log.v(TAG, "LAC : " + mLac);
        Log.v(TAG, "IMSI : " + mImsi);
        Log.v(TAG, "Phone model : " + mPhoneModel);
        Log.v(TAG, "signal strength : " + mSignalStrength);
        Log.v(TAG, "Battery level : " + mBatteryLevel);
    }

    // Todo : remove the getters, only for debugging
    /* Getter */

    public int getCid() {
        return mCid;
    }

    public int getLac() {
        return mLac;
    }

    public String getMcc() {
        return mMcc;
    }

    public String getMnc() {
        return mMnc;
    }

    public String getImsi() {
        return mImsi;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public String getPhoneModel() {
        return mPhoneModel;
    }

    public String getAndroidVersion() {
        return mAndroidVersion;
    }

    public Long getTimeStamp() {
        return mTimeStamp;
    }

    public float getSignalStrength() {
        return mSignalStrength;
    }

    public float getBatteryLevel() {
        return mBatteryLevel;
    }
}
