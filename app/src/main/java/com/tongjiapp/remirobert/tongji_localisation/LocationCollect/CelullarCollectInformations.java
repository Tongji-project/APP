package com.tongjiapp.remirobert.tongji_localisation.LocationCollect;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.telephony.CellInfoGsm;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by remirobert on 19/04/16.
 */
public class CelullarCollectInformations {

    private static final String TAG = "CelullarCollect";

    private TelephonyManager mTelephonyManager;
    private Context mContext;
    private CelullarInformation mCelullarInformation;

    public CelullarCollectInformations(Context context) {
        mContext = context;
        mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void getInformations() {

        listenTelephonyManager();


//        return Observable.create(new Observable.OnSubscribe<CelullarInformation>() {
//            @Override
//            public void call(Subscriber<? super CelullarInformation> subscriber) {
//                listenTelephonyManager();
//                subscriber.onNext(mCelullarInformation);
//                subscriber.onCompleted();
//            }
//        });
    }

    private void listenTelephonyManager() {
        Log.v(TAG, "listen informations");
        mTelephonyManager.listen(phoneStateListener,
                        PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                        PhoneStateListener.LISTEN_CALL_STATE |
                        PhoneStateListener.LISTEN_CELL_LOCATION |
                        PhoneStateListener.LISTEN_DATA_ACTIVITY |
                        PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                        PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR |
                        PhoneStateListener.LISTEN_SERVICE_STATE |
                        PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        mCelullarInformation = new CelullarInformation(mTelephonyManager);
        mCelullarInformation.setBatteryLevel(getBatteryLevel());

        mCelullarInformation.description();
    }

    private float getBatteryLevel() {
        Intent batteryIntent = mContext.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return ((float)level / (float)scale) * 100.0f;
    }

    public PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallForwardingIndicatorChanged(boolean cfi) {}
        public void onCallStateChanged(int state, String incomingNumber) {}
        public void onCellLocationChanged(CellLocation location) {}
        public void onDataActivity(int direction) {}
        public void onDataConnectionStateChanged(int state) {}
        public void onMessageWaitingIndicatorChanged(boolean mwi) {}
        public void onServiceStateChanged(ServiceState serviceState) {}

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int signal = signalStrength.getGsmSignalStrength();
            signal = (2 * signal) - 113;
            mCelullarInformation.setSignalStrength(signal);
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private float getSignalStrength() {
        CellInfoGsm cellinfogsm = (CellInfoGsm)mTelephonyManager.getAllCellInfo().get(0);
        CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
        return cellSignalStrengthGsm.getDbm();
    }
}
