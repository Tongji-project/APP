package com.tongjiapp.remirobert.tongji_localisation;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;

/**
 * Created by remirobert on 02/05/16.
 */
public class Record extends RealmObject {
    private RecordDevice mDevice;
    private DLocation mGpsLocation;
    private DLocation mNetworkLocation;
    private String mTimeStamp;

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("timestamp", getTimeStamp());
        return json;
    }


    public Record() {}

    public RecordDevice getDevice() {
        return mDevice;
    }

    public void setDevice(RecordDevice device) {
        mDevice = device;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        mTimeStamp = timeStamp;
    }

    public DLocation getGpsLocation() {
        return mGpsLocation;
    }

    public void setGpsLocation(DLocation gpsLocation) {
        mGpsLocation = gpsLocation;
    }

    public DLocation getNetworkLocation() {
        return mNetworkLocation;
    }

    public void setNetworkLocation(DLocation networkLocation) {
        mNetworkLocation = networkLocation;
    }
}
