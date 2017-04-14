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


import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.Toast;

import com.example.kriti.aninterface.Utilities.DeviceReader;
import com.example.kriti.aninterface.Utilities.DeviceWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity {

    WifiManager wifiManager;
    List<ScanResult> wifiList;
    ArrayList<ScanResult> selectedWifi;
    WifiAccess wifiAccess;
    DeviceReader deviceReader;
    DeviceReader deviceReader1;
    DeviceReader deviceReader2;
    DeviceReader deviceReader3;
    Context context;
    MyMap mapView;
    Toolbar tb;
    String[] From = {"My Loc", "RN 30", "RN 31", "RN 32", "RN 33", "RN 34", "RN 35", "RN 36", "RN 37", "RN 38", "RN 39"};
    String[] To = {" RN 30", " RN 31", " RN 32", " RN 33", " RN 34", " RN 35", " RN 36", " RN 37", " RN 38", " RN 39"};
    String glassReadings=null,pillarReadings=null,doorReadings=null,switchReadings=null;
    DeviceWriter deviceWriter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceWriter = new DeviceWriter(this);

        mapView = (MyMap) findViewById(R.id.canvas);
        tb = (Toolbar) findViewById(R.id.toolbar);

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


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiAccess = new WifiAccess();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED |
                        checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED |
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x12345);
        } else {
            getSignalStrength();
        }

        try {
            deviceReader = new DeviceReader("glass.txt");
            glassReadings = deviceReader.initialize();
            deviceReader1 = new DeviceReader("pillar.txt");
            pillarReadings = deviceReader1.initialize();
            deviceReader2 = new DeviceReader("switch.txt");
            switchReadings = deviceReader2.initialize();
            deviceReader3 = new DeviceReader("door.txt");
            doorReadings = deviceReader3.initialize();

        } catch (IOException e) {
            e.printStackTrace();
            Log.w("TAGU", "vailable");
        }

        boolean res = deviceReader.is_ExternalStorageWriteable();
        if (res == true) {
            Log.w("TAGU", "storage available");
        }
    }

    public void foundPosition(int i, int j){


    }

    public void compare(ArrayList<ScanResult> arrayList){
        ScanResult scanGlass = null, scanDoor=null, scanSwich=null, scanPillar=null;
        int glass[][] = new int[7][9];
        int door[][] = new int[7][9];
        int swich[][] = new int[7][9];
        int pillar[][] = new int[7][9];

        for(int i=0;i<4;i++){
            if(arrayList.get(i).SSID.equalsIgnoreCase("glass")){
                scanGlass = arrayList.get(i);
            }
            else if(arrayList.get(i).SSID.equalsIgnoreCase("door")){
                scanDoor = arrayList.get(i);
            }
            else if(arrayList.get(i).SSID.equalsIgnoreCase("switch")){
                scanSwich = arrayList.get(i);
            }
            else if(arrayList.get(i).SSID.equalsIgnoreCase("pillar")) {
                scanPillar = arrayList.get(i);
            }
        }

        for(int i=0;i<7;i++){
            for(int j=0;j<9;j++){
                if(glass[i][j] == scanGlass.level && door[i][j] == scanDoor.level &&
                        swich[i][j]==scanSwich.level && pillar[i][j] ==scanPillar.level) foundPosition(i,j);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getSignalStrength();
        }
    }


    protected void onStop() {
        try {
            unregisterReceiver(wifiAccess);
        } catch (Exception e) {

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
            Toast.makeText(getApplicationContext(), "Wifi Disabled, Enabling", Toast.LENGTH_LONG).show();

            wifiManager.setWifiEnabled(true);

            Toast.makeText(getApplicationContext(), "Wifi Enabled", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(getApplicationContext(), "Wifi is Enabled", Toast.LENGTH_LONG).show();

        registerReceiver(wifiAccess, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Log.d("WIFI", "Start scan");
    }

    public class WifiAccess extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            selectedWifi = new ArrayList<ScanResult>();
            Log.d("WIFI", "on Recieve");
            wifiList = wifiManager.getScanResults();
            String s = Integer.toString(wifiList.size());
            Toast.makeText(getApplicationContext(), "Networks found : "+s, Toast.LENGTH_LONG).show();
            if (wifiList.isEmpty()) {
                Toast.makeText(getApplicationContext(), "No network found", Toast.LENGTH_LONG).show();
            }
            else {
                for (int i=0;i<wifiList.size();i++) {
                    if (wifiList.get(i).SSID.equalsIgnoreCase("fancyfish") || wifiList.get(i).SSID.equalsIgnoreCase("door") ||
                            wifiList.get(i).SSID.equalsIgnoreCase("pillar") || wifiList.get(i).SSID.equalsIgnoreCase("switch")) {
                        selectedWifi.add(wifiList.get(i));
                    }
                }
                if (selectedWifi.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No network found from d,p,s,g", Toast.LENGTH_LONG).show();
                } else {
                    try{
                        for(int i=0;i<selectedWifi.size();i++) {
                            Toast.makeText(context, selectedWifi.get(i).SSID + " " + selectedWifi.get(i).level, Toast.LENGTH_LONG).show();
                        }
                    }catch (NoSuchElementException e){
                        Log.w("TAGU","EXCEPTION in selected wifi list");
                    }
                    compare(selectedWifi);
                }
            }
        }
    }
}