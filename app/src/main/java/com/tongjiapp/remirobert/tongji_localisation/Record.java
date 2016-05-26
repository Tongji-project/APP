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
        JSONObject networkLocation = new JSONObject();
        JSONObject gpsLocation = new JSONObject();
        JSONObject state = new JSONObject();
        JSONObject signal = new JSONObject();

        networkLocation.put("latitude", getNetworkLocation().getLat());
        networkLocation.put("longitude", getNetworkLocation().getLon());
        gpsLocation.put("latitude", getNetworkLocation().getLat());
        gpsLocation.put("longitude", getNetworkLocation().getLon());

        state.put("battery_usage", getDevice().getBatteryLevel());
        state.put("signal_strength", getDevice().getSignalStrength());

        signal.put("lac", getDevice().getLac());
        signal.put("cid", getDevice().getCid());
        signal.put("country_iso", getDevice().getCountryIso());

        json.put("gps_location", gpsLocation);
        json.put("network_location", networkLocation);
        json.put("timestamp", getTimeStamp());
        json.put("state", state);
        json.put("signal", signal);
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
