package com.tongjiapp.remirobert.tongji_localisation.LocationCollect;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import rx.Subscriber;

/**
 * Created by remirobert on 19/04/16.
 */
public class LocationManager {

    private static final String TAG = "LocationManager";

    private LocalisationCollectGPS mLocalisationCollectGPS;
    private CelullarCollectInformations mCelullarCollectInformations;
    private static LocationManager mInstance = null;

    private LocationManager() {
    }

    public static LocationManager getInstance() {
        if (mInstance == null) {
            mInstance = new LocationManager();
        }
        return mInstance;
    }

    public void getLocation(Context context) {
        mLocalisationCollectGPS = new LocalisationCollectGPS(context);
        mCelullarCollectInformations = new CelullarCollectInformations(context);

        // TODO : bind two signals, and return a merged model to bind in the activity's interface

        mLocalisationCollectGPS.getLocation().subscribe(new Subscriber<Location>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onNext(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    Log.v(TAG, "get location : " + lat + " : " + lon);
                }
                else {
                    Log.e(TAG, "Location null");
                }
            }
        });
        mCelullarCollectInformations.getInformations();
    }
}
