package com.example.myapplication;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.CellInfoGsm;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private double latitude = 0.0;
    private double longitude = 0.0;
    private TextView info;
    private LocationManager locationManager;
    private EditText show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        show =(EditText) findViewById(R.id.main_et_show);
        // get LocationManager service
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // get get Last Known Location
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // show the information of location in EditText
        updateView(location);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        // 当GPS定位信息发生改变时，更新位置
                        updateView(location);
                    }
                    @Override
                    public void onProviderDisabled(String provider) {
                        updateView(null);
                    }
                    @Override
                    public void onProviderEnabled(String provider) {
                        // 当GPS LocationProvider可用时，更新位置
                        updateView(locationManager
                                .getLastKnownLocation(provider));
                    }
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                });

//        while(location==null) {
//            //update when the GPS signal
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            if(location!=null){
//                System.out.println("hahahaahahahhahahahha :"+location.getAltitude());
//            }
//        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    }
//    private Location updateView(Location location) {
//        System.out.println("--------GPS--test--------");
//        String latLongString;
//        double lat = 0;
//        double lng=0;
//
//        if (location != null) {
//            lat = location.getLatitude();
//            lng = location.getLongitude();
//            latLongString = "纬度:" + lat + "\n经度:" + lng;
//            System.out.println("经度："+lng+"纬度："+lat);
//        } else {
//            latLongString = "无法获取地理信息，请稍后...";
//            System.out.println("waiting>>>>>>>");
//        }
//        if(lat!=0){
//            System.out.println("--------feed back----------"+ String.valueOf(lat));
//        }
//
//        Toast.makeText(getApplicationContext(), latLongString, Toast.LENGTH_SHORT).show();
//
//        return location;
//
//    }
    private void updateView(Location location) {
        System.out.println("调用阿萨阿萨撒阿萨德啊圈啊爱谁谁啊阿萨");

        if (location != null) {
            StringBuffer sb = new StringBuffer();

            sb.append("GPS information：\nLongitude：");

            sb.append(location.getLongitude());

            sb.append("\nLatitude：");

            sb.append(location.getLatitude());
            sb.append("\nAltitude：");

            sb.append(location.getAltitude());

            sb.append("\nSpeed：");

            sb.append(location.getSpeed());

            sb.append("\nBearing：");

            sb.append(location.getBearing());

            sb.append("\nAccuracy：");

            sb.append(location.getAccuracy());

            show.setText(sb.toString());

        } else {

            // if Location is empty then clear the EditText
            show.setText("");
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
