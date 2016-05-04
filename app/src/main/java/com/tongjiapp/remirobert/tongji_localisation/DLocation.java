package com.tongjiapp.remirobert.tongji_localisation;

import io.realm.RealmObject;

/**
 * Created by remirobert on 02/05/16.
 */
public class DLocation extends RealmObject {
    private double mLat;
    private double mLon;

    public DLocation() {

    }

    //Todo fetch location for GPS and network
    public DLocation(android.location.Location location) {
        mLat = location.getLatitude();
        mLon = location.getLongitude();
    }

    public double getLat() {
        return mLat;
    }

    public double getLon() {
        return mLon;
    }
}
