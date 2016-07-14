package com.tongjiapp.remirobert.tongji_localisation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocalisationActivity extends AppCompatActivity {

    private static final String TAG = "LocalisationActivity";
    private static final int REQUEST_LOCATION = 10;

    private TextView mLocationGPSTextView;
    private TextView mLocationNetworkTextView;
    private TextView mSignalStrenghtTextView;
    private TextView mCidTextView;
    private TextView mLacTextView;
    private TextView mIsoCountryTextView;
    private TextView mBatteryTextView;
    private TextView mDeviceIdTextView;
    private TextView mMccTextView;
    private TextView mMncTextView;
    private TextView mOperatorTextView;
    private TextView mDeviceModelTextView;
    private TextView mAndroidVersionTextView;
    private TextView mBatteryCapacity;

    private Button mButtonLocalisation;

    private RecordDeviceManager mRecordDeviceManager;

    private void recordNewLocation() {
        mRecordDeviceManager.newRecord(new RecordDeviceManagerListener() {
            @Override
            public void onRecordDeviceInformations(Record record, Device device) {
                Log.v(TAG, "did record new device informations");
                double latGPS = record.getGpsLocation().getLat();
                double lonGPS = record.getGpsLocation().getLon();

                double latNet = record.getNetworkLocation().getLat();
                double lonNet = record.getNetworkLocation().getLon();

                mLocationGPSTextView.setText("" + latGPS + ":" + lonGPS);
                mLocationNetworkTextView.setText("" + latNet + ":" + lonNet);

                mSignalStrenghtTextView.setText("" + record.getDevice().getSignalStrength());
                mCidTextView.setText("" + record.getDevice().getCid());
                mLacTextView.setText("" + record.getDevice().getLac());
                mIsoCountryTextView.setText("" + record.getDevice().getCountryIso());
                mBatteryTextView.setText("" + record.getDevice().getBatteryLevel());


                mMccTextView.setText("" + device.getMcc());
                mMncTextView.setText("" + device.getMnc());

                mDeviceIdTextView.setText("" + device.getDeviceId());
                mOperatorTextView.setText("" + device.getOperatorName());

                mDeviceModelTextView.setText("" + device.getPhoneModel());
                mAndroidVersionTextView.setText("" + device.getAndroidVersion());

                mBatteryCapacity.setText("" + record.getDevice().getBatteryCapacity());
            }
        });
    }

    private void checkPermissionUser() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_LOCATION);
        } else {
            recordNewLocation();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        mLocationGPSTextView = (TextView) findViewById(R.id.location_text_view);
        mLocationNetworkTextView = (TextView) findViewById(R.id.location_network_text_view);
        mSignalStrenghtTextView = (TextView) findViewById(R.id.signal_strenght_text_view);
        mCidTextView = (TextView) findViewById(R.id.cid_text_view);
        mLacTextView = (TextView) findViewById(R.id.lac_text_view);
        mIsoCountryTextView = (TextView) findViewById(R.id.country_iso_text_view);
        mBatteryTextView = (TextView) findViewById(R.id.battery_text_view);
        mDeviceIdTextView = (TextView) findViewById(R.id.device_id_text_view);
        mMccTextView = (TextView) findViewById(R.id.mcc_text_view);
        mMncTextView = (TextView) findViewById(R.id.mnc_text_view);
        mOperatorTextView = (TextView) findViewById(R.id.operator_text_view);
        mAndroidVersionTextView = (TextView) findViewById(R.id.android_text_view);
        mDeviceModelTextView = (TextView) findViewById(R.id.model_device_text_view);
        mBatteryCapacity = (TextView) findViewById(R.id.battery_capacity_text_view);

        mButtonLocalisation = (Button) findViewById(R.id.button_get_localisation);
        mRecordDeviceManager = new RecordDeviceManager(this);

        mButtonLocalisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationNetworkTextView.setText("");
                mLocationGPSTextView.setText("");
                mSignalStrenghtTextView.setText("");
                checkPermissionUser();
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recordNewLocation();
            } else {
                Toast.makeText(this, "Error permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
