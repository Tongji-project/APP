package com.tongjiapp.remirobert.tongji_localisation;

import io.realm.RealmObject;

/**
 * Created by remirobert on 02/05/16.
 */
public class RecordDevice extends RealmObject {

    private int mBatteryLevel;
    private int mSignalStrength;
    private int mCid;
    private int mLac;
    private String mCountryIso;

    public RecordDevice() {}

    public int getBatteryLevel() {
        return mBatteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        mBatteryLevel = batteryLevel;
    }

    public int getSignalStrength() {
        return mSignalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        mSignalStrength = signalStrength;
    }

    public int getCid() {
        return mCid;
    }

    public void setCid(int cid) {
        mCid = cid;
    }

    public int getLac() {
        return mLac;
    }

    public void setLac(int lac) {
        mLac = lac;
    }

    public String getCountryIso() {
        return mCountryIso;
    }

    public void setCountryIso(String countryIso) {
        mCountryIso = countryIso;
    }
}
