package com.example.kriti.aninterface;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.Toast;
import com.example.kriti.aninterface.Utilities.DeviceWriter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    WifiManager wifiManager;
    List<ScanResult> wifiList;
    WifiAccess wifiAccess;
    Context context;
    MyMap mapView;
    Toolbar tb;
    String[] From = {"My Loc", "RN 30", "RN 31", "RN 32", "RN 33", "RN 34", "RN 35", "RN 36", "RN 37", "RN 38", "RN 39"};
    String[] To = {" RN 30", " RN 31", " RN 32", " RN 33", " RN 34", " RN 35", " RN 36", " RN 37", " RN 38", " RN 39"};
    DeviceWriter deviceWriter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceWriter = new DeviceWriter(this);
        mapView = (MyMap) findViewById(R.id.canvas);
        tb = (Toolbar) findViewById(R.id.toolbar);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiAccess = new WifiAccess();

        setSupportActionBar(tb);
        getSupportActionBar().setTitle("");

        Spinner fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, To);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        Spinner toSpinner = (Spinner) findViewById(R.id.toSpinner);
        ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, From);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (
                checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED |
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, 0x12345);
        }
        else {
            getSignalStrength();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getSignalStrength();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onStop()
    {
        try{
        unregisterReceiver(wifiAccess);
        }
        catch (Exception e){

        }

        super.onStop();
    }

    public void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(wifiAccess);

    }

    public void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(wifiAccess, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }


    public void getSignalStrength() {


        if (wifiManager.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "wifi disabled, enabling", Toast.LENGTH_LONG).show();

            wifiManager.setWifiEnabled(true);

            Toast.makeText(getApplicationContext(), "wifi enabled", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(getApplicationContext(), "wifi enabled", Toast.LENGTH_LONG).show();

        registerReceiver(wifiAccess, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Log.d("WIFI", "Start scan");
    }


    public class WifiAccess extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("WIFI", "on Recieve");

            wifiList = wifiManager.getScanResults();
            String s = Integer.toString(wifiList.size());
            Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();

            if (wifiList.isEmpty()) {
                Toast.makeText(getApplicationContext(),"kat gya", Toast.LENGTH_LONG).show();
            }
            else {
                for (ScanResult scanResult : wifiList) {
                    int rssi = wifiManager.getConnectionInfo().getRssi();
                    //Toast.makeText(context, rssi, Toast.LENGTH_LONG).show();
                    Toast.makeText(context, scanResult.SSID + " " + scanResult.level, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
