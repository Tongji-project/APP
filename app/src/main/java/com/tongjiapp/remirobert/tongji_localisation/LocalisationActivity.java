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
    private Button mButtonLocalisation;

    private RecordDeviceManager mRecordDeviceManager;

    private void recordNewLocation() {
        mRecordDeviceManager.newRecord(new RecordDeviceManagerListener() {
            @Override
            public void onRecordDeviceInformations(Record record) {
                Log.v(TAG, "did record new device informations");
                double latGPS = record.getGpsLocation().getLat();
                double lonGPS = record.getGpsLocation().getLon();

                double latNet = record.getNetworkLocation().getLat();
                double lonNet = record.getNetworkLocation().getLon();

                mLocationGPSTextView.setText("GPS : " + latGPS + ":" + lonGPS);
                mLocationNetworkTextView.setText("GPS : " + latNet + ":" + lonNet);
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
        mButtonLocalisation = (Button) findViewById(R.id.button_get_localisation);
        mRecordDeviceManager = new RecordDeviceManager(this);

        mButtonLocalisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationNetworkTextView.setText("");
                mLocationGPSTextView.setText("");
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
