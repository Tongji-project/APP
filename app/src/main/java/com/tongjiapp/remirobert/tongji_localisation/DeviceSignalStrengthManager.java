package com.tongjiapp.remirobert.tongji_localisation;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

/**
 * Created by remirobert on 02/05/16.
 */

interface DeviceSignalStrengthManagerListener {
    void onReceiveSignalStrength(int signalStrength, int signalStrengthPercent);
}

public class DeviceSignalStrengthManager {

    private static final int UNKNOW_CODE = 99;
    private static final int MAX_SIGNAL_DBM_VALUE = 31;

    private TelephonyManager mTelephonyManager;
    private PhoneStateListener mPhoneStateListener;

    static private int getSignalStrengthInPercent(int signalStrength) {
        return (int) ((float) signalStrength / MAX_SIGNAL_DBM_VALUE * 100);
    }

    static private int getSignalValue(SignalStrength signalStrength) {
        int signal = signalStrength.getGsmSignalStrength();
        return signal = (2 * signal) - 113;
    }

    public DeviceSignalStrengthManager(TelephonyManager telephonyManager) {
        mTelephonyManager = telephonyManager;
    }

    public void getSignalStrength(final DeviceSignalStrengthManagerListener listener) {
        mPhoneStateListener = new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);

                if (null != signalStrength && signalStrength.getGsmSignalStrength() != UNKNOW_CODE) {
                    int signalStrengthPercent = getSignalStrengthInPercent(signalStrength.getGsmSignalStrength());
                    listener.onReceiveSignalStrength(getSignalValue(signalStrength), signalStrengthPercent);
                }
                mTelephonyManager.listen(mPhoneStateListener, LISTEN_NONE);
            }
        };
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }
}
