package com.tongjiapp.remirobert.tongji_localisation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;


/**
 * Created by remirobert on 02/05/16.
 */

interface DeviceLocationManagerListener {
    void onReveiceLocation(DLocation location);
}

public class DeviceLocationManager implements LocationListener {

    private static final long MIN_DISTANCE_FOR_UPDATE = 1;
    private static final long MIN_TIME_FOR_UPDATE = 0;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 10;

    private Context mContext;
    private LocationManager mLocationManager;
    private DeviceLocationManagerListener mDeviceLocationManagerListener;

    public void onLocationChanged(Location location) {
        mDeviceLocationManagerListener.onReveiceLocation(new DLocation(location));
        stopLocationUpdate();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        mDeviceLocationManagerListener.onReveiceLocation(null);
        stopLocationUpdate();
    }


    private void stopLocationUpdate() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(this);
        }
    }

    public DeviceLocationManager(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public void getLocation(String provider, DeviceLocationManagerListener listener) {
        mDeviceLocationManagerListener = listener;
        if (mLocationManager == null) {
            listener.onReveiceLocation(null);
            return;
        }
        try {
            if (!mLocationManager.isProviderEnabled(provider)) {
                listener.onReveiceLocation(null);
                return;
            }
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                listener.onReveiceLocation(null);
            } else {
                mLocationManager.requestLocationUpdates(provider, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
                Location location = mLocationManager.getLastKnownLocation(provider);
                if (location != null) {
                    listener.onReveiceLocation(new DLocation(location));
                    stopLocationUpdate();
                }
            }
        } catch (Exception e) {
            listener.onReveiceLocation(null);
        }
    }
}
