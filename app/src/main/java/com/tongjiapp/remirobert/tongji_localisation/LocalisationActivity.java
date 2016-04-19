package com.tongjiapp.remirobert.tongji_localisation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tongjiapp.remirobert.tongji_localisation.LocationCollect.LocationManager;

public class LocalisationActivity extends AppCompatActivity {

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 10;
    private static final String TAG = "LocalisationActivity";

    private TextView mLocationTextView;
    private Button mButtonLocalisation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        mLocationTextView = (TextView)findViewById(R.id.location_text_view);
        mButtonLocalisation = (Button)findViewById(R.id.button_get_localisation);
        mLocationTextView.setText("localisation");

        mButtonLocalisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocalisation();
            }
        });

    }

    private void findLocalisation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
        else {
            getLocation();
        }
    }

    private void getLocation() {
        LocationManager.getInstance().getLocation(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                    getLocation();
                    Toast.makeText(this, "Location COARSE OKAY", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Need your location! CARSE", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
