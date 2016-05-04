package com.tongjiapp.remirobert.tongji_localisation;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by remirobert on 02/05/16.
 */
public class Device extends RealmObject {

    @Required
    private String mDeviceId;
    private int mMcc;
    private int mMnc;
    private String mOperatorName;
    private String mPhoneModel;
    private int mAndroidVersion;

    public Device() {}

    public String getOperatorName() {
        return mOperatorName;
    }

    public void setOperatorName(String operatorName) {
        mOperatorName = operatorName;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }

    public int getMcc() {
        return mMcc;
    }

    public void setMcc(int mcc) {
        mMcc = mcc;
    }

    public int getMnc() {
        return mMnc;
    }

    public void setMnc(int mnc) {
        mMnc = mnc;
    }

    public String getPhoneModel() {
        return mPhoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        mPhoneModel = phoneModel;
    }

    public int getAndroidVersion() {
        return mAndroidVersion;
    }

    public void setAndroidVersion(int androidVersion) {
        mAndroidVersion = androidVersion;
    }
}
