package com.tongjiapp.remirobert.tongji_localisation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class LocalisationActivity extends AppCompatActivity {

    private static final String TAG = "LocalisationActivity";

    private TextView mLocationTextView;
    private Button mButtonLocalisation;

    private RecordDeviceManager mRecordDeviceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        mLocationTextView = (TextView) findViewById(R.id.location_text_view);
        mButtonLocalisation = (Button) findViewById(R.id.button_get_localisation);
        mLocationTextView.setText("localisation 4");

        mRecordDeviceManager = new RecordDeviceManager(this);
        mRecordDeviceManager.newRecord(new RecordDeviceManagerListener() {
            @Override
            public void onRecordDeviceInformations(Record record) {
                Log.v(TAG, "did record new device informations");
                mLocationTextView.setText("battery : " + record.getDevice().getBatteryLevel());
            }
        });
    }
}
