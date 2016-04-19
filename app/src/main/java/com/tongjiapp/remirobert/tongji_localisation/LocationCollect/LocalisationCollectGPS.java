package com.tongjiapp.remirobert.tongji_localisation.LocationCollect;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by remirobert on 18/04/16.
 */

public class LocalisationCollectGPS {

    private static final long MIN_DISTANCE_FOR_UPDATE = 1;//1m
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 2;//2 ms
    private static final String TAG = "LocalisationCollectGPS";

    protected LocationManager mLocationManager;

    public enum LocalisationCollectGPSProvider {
        GPS, NETWORK, BEST
    }
    private Context mContext;
    private LocalisationCollectGPSProvider mProvider = LocalisationCollectGPSProvider.BEST;
    private long mMinDistanceUpdate = MIN_DISTANCE_FOR_UPDATE;
    private long mMinTimeUpdate = MIN_TIME_FOR_UPDATE;

    /* Getter */

    public long getMinTimeUpdate() {
        return mMinTimeUpdate;
    }

    public long getMinDistanceUpdate() {
        return mMinDistanceUpdate;
    }

    public LocalisationCollectGPSProvider getProvider() {
        return mProvider;
    }

    /* Setter */

    public void setProvider(LocalisationCollectGPSProvider provider) {
        mProvider = provider;
    }

    public void setMinDistanceUpdate(long minDistanceUpdate) {
        mMinDistanceUpdate = minDistanceUpdate;
    }

    public void setMinTimeUpdate(long minTimeUpdate) {
        mMinTimeUpdate = minTimeUpdate;
    }

    /* Public Constructor */

    public LocalisationCollectGPS(Context context) {
        mContext = context;
        mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    /* Method localisation */

    public Observable<Location> getLocation() {
        if (isLocationEnabled()) {
            return fetchLocation();
        }
        return Observable.just(null);
    }

    private boolean isLocationEnabled() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        switch (mProvider) {
            case BEST:
                return isGPSEnabled && isNetworkEnabled;
            case GPS:
                return isGPSEnabled;
            case NETWORK:
                return isNetworkEnabled;
        }
        return false;
    }

    private Observable<Location> fetchLocation() {
        return Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(Subscriber<? super Location> subscriber) {

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                                                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                                                                != PackageManager.PERMISSION_GRANTED) {
                    subscriber.onNext(null);
                    return;
                }

                LocalisationCollectGPSListener listener = new LocalisationCollectGPSListener(subscriber);

                switch (mProvider) {
                    case BEST: {
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_FINE);
                        criteria.setAltitudeRequired(false);
                        criteria.setBearingRequired(false);
                        criteria.setCostAllowed(true);
                        criteria.setPowerRequirement(Criteria.POWER_LOW);

                        String provider = mLocationManager.getBestProvider(criteria, true);
                        if(provider != null) {
                            mLocationManager.requestLocationUpdates(provider, mMinDistanceUpdate, mMinTimeUpdate, listener);
                            subscriber.onNext(mLocationManager.getLastKnownLocation(provider));
                        }
                        break;
                    }
                    case GPS: {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mMinDistanceUpdate, mMinTimeUpdate, listener);
                        subscriber.onNext(mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER));
                    }
                    case NETWORK: {
                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mMinDistanceUpdate, mMinTimeUpdate, listener);
                        subscriber.onNext(mLocationManager.getLastKnownLocation(mLocationManager.NETWORK_PROVIDER));
                    }
                }
            }
        });
    }
}
