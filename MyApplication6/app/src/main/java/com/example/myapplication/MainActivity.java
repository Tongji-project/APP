package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {

    Button btnGPSShowLocation;
    Button btnNWShowLocation;

    AppLocationService appLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //注册一个接受广播类型
        registerReceiver(new BatteryBroadcastReceiver(), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
           PhoneStateListener phoneStateListener = new PhoneStateListener() {
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
                signal = (2 * signal) - 113; // -> dBm
                System.out.println("signal" +signal);
            }
        };
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                        PhoneStateListener.LISTEN_CALL_STATE |
                        PhoneStateListener.LISTEN_CELL_LOCATION |
                        PhoneStateListener.LISTEN_DATA_ACTIVITY |
                        PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                        PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR |
                        PhoneStateListener.LISTEN_SERVICE_STATE |
                        PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);


        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

        Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = tsLong.toString();
        System.out.println("timeStamp " +timeStamp);

        String Deviceid =telephonyManager.getDeviceId();
        System.out.println("device_id " +Deviceid);
        String  IMSI;
        IMSI = telephonyManager.getSubscriberId();
        System.out.println("IMSI :" +IMSI);
        // Device model
        String PhoneModel = android.os.Build.MODEL;

        // Android version
        String AndroidVersion = android.os.Build.VERSION.RELEASE;
        //info
        System.out.println("PhoneModel :" +PhoneModel);
        System.out.println("AndroidVersion :" +AndroidVersion);

        int cid = cellLocation.getCid();
        int lac = cellLocation.getLac();
        System.out.println("cid " +cid);
        System.out.println("lac " +lac);
        String networkOperator = telephonyManager.getNetworkOperator();
        if (TextUtils.isEmpty(networkOperator) == false) {
            int mcc = Integer.parseInt(networkOperator.substring(0, 3));
            int mnc = Integer.parseInt(networkOperator.substring(3));
            System.out.println("mcc " +mcc);
            System.out.println("mnc " +mnc);
        }



        appLocationService = new AppLocationService(
                MainActivity.this);
        /////////////////////


        Location GpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
        if (GpsLocation != null) {

            double latitude = GpsLocation.getLatitude();
            double longitude = GpsLocation.getLongitude();
            System.out.println("latitude " +latitude);
            System.out.println("longitude " +longitude);


        }


        Location netLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

        if (netLocation != null) {
            double latitude = netLocation.getLatitude();
            double longitude = netLocation.getLongitude();
            System.out.println("latitude " +latitude);
            System.out.println("longitude " +longitude);
        }

        ///////////////////

        btnGPSShowLocation = (Button) findViewById(R.id.btnGPSShowLocation);
        btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Location gpsLocation = appLocationService
                        .getLocation(LocationManager.GPS_PROVIDER);

                if (gpsLocation != null) {
                    double latitude = gpsLocation.getLatitude();
                    double longitude = gpsLocation.getLongitude();
                    Toast.makeText(
                            getApplicationContext(),
                            "Mobile Location (GPS): \nLatitude: " + latitude
                                    + "\nLongitude: " + longitude,
                            Toast.LENGTH_LONG).show();
                } else {
                    showSettingsAlert("GPS");
                }

            }
        });

        btnNWShowLocation = (Button) findViewById(R.id.btnNWShowLocation);
        btnNWShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

                if (nwLocation != null) {
                    double latitude = nwLocation.getLatitude();
                    double longitude = nwLocation.getLongitude();
                    Toast.makeText(
                            getApplicationContext(),
                            "Mobile Location (NW): \nLatitude: " + latitude
                                    + "\nLongitude: " + longitude,
                            Toast.LENGTH_LONG).show();
                } else {
                    showSettingsAlert("NETWORK");
                }

            }
        });

    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /**接受电量改变广播*/
    class BatteryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){

                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                int curPower = (level * 100 / scale);
                System.out.println("curPower :"+curPower);
                }
            }
        }
}

