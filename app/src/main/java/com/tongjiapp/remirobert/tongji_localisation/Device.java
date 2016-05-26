package com.tongjiapp.remirobert.tongji_localisation;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by remirobert on 02/05/16.
 */
public class Device extends RealmObject {

    @PrimaryKey
    private String mDeviceId;
    private int mMcc;
    private int mMnc;
    private String mOperatorName;
    private String mPhoneModel;
    private int mAndroidVersion;
    private boolean uploaded;

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("deviceId", getDeviceId());
        json.put("mcc", getMcc());
        json.put("mnc", getMnc());
        json.put("operator", getOperatorName());
        json.put("androidVersion", getAndroidVersion());
        json.put("phoneModel", getPhoneModel());
        return json;
    }

    public Device() {
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

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
