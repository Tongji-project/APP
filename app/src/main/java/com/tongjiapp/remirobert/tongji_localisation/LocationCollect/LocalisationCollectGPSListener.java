package com.tongjiapp.remirobert.tongji_localisation.LocationCollect;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import rx.Subscriber;

/**
 * Created by remirobert on 19/04/16.
 */
public class LocalisationCollectGPSListener implements LocationListener {

    private Subscriber<? super Location> mSubscriber;

    public LocalisationCollectGPSListener(Subscriber<? super Location> subscriber) {
        mSubscriber = subscriber;
    }

    public void onLocationChanged(Location location) {
        mSubscriber.onNext(location);
        mSubscriber.onCompleted();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
        mSubscriber.onError(null);
    }
}
