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

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kriti.aninterface.Utilities.DeviceReader;
import com.example.kriti.aninterface.Utilities.DeviceWriter;
import com.example.kriti.aninterface.Utilities.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    Context mContext;
    MyMap mapView;
    Toolbar tb;
    ImageButton goDirect;
    int graph[][] = new int[][]{

            {0, 2, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 2, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 0, 2, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 2, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 2, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 2, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 1, 2, 0, 2, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 2, 1, 0, 0, 2, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 0, 0, 1, 2, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 2, 0, 2, 1, 2, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 2, 0, 2, 1, 2},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 2, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 2, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 2, 0, 2, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 2, 0, 2},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 2, 0}

    };

    String[] From = {"Stall 1", "Stall 2", "Stall 3", "Stall 4", "Stall 5", "Stall 6"};
    String[] To = {"Stall 1", "Stall 2", "Stall 3", "Stall 4", "Stall 5", "Stall 6"};
    String fromValue, toValue;
    String glassReadings = null, pillarReadings = null, doorReadings = null, switchReadings = null;
    String[] glassR, pillarR, doorR, swichR;
    int glass[][] = new int[7][9];
    int door[][] = new int[7][9];
    int swich[][] = new int[7][9];
    int pillar[][] = new int[7][9];
    DeviceWriter deviceWriter;
    Route route;
    Spinner fromSpinner;
    int destination = 0;
    int source = 0;
    Node node;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MyMap) findViewById(R.id.canvas);
        tb = (Toolbar) findViewById(R.id.toolbar);
        goDirect = (ImageButton) findViewById(R.id.navigatebutton);

        setSupportActionBar(tb);
        getSupportActionBar().setTitle("");
        mContext = this;

        fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        final ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, From);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);
        //fromValue = fromSpinner.getSelectedItem().toString();
        //updateSource(fromValue);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Toast.makeText(mContext,"Clicked " + position, Toast.LENGTH_LONG).show();
                fromValue = From[position];
                //Log.w("M_APP",position + " " );
                updateSource(fromValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Toast.makeText(mContext,"NOthing",Toast.LENGTH_LONG).show();
                fromValue = From[0];
                //Log.w("M_APP",position + " " );
                updateSource(fromValue);
            }
        });


        final Spinner toSpinner = (Spinner) findViewById(R.id.toSpinner);
        ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, To);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);
/*
        toValue = toSpinner.getSelectedItem().toString();
        updateDestination(toValue);
*/

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                toValue = To[position];
                updateDestination(toValue);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                toValue = To[0];
                updateDestination(toValue);
            }

        });

        route = new Route(mapView);

        goDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                route.routing(graph, source, destination);
                route.drawRouteOnScreen(source);

            }
        });

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiAccess = new WifiAccess();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED |
                        checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED |
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, 0x12345);
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


        glassR = glassReadings.split(" ");
        doorR = doorReadings.split(" ");
        swichR = switchReadings.split(" ");
        pillarR = pillarReadings.split(" ");
        try {

            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    glass[i][j] = Integer.parseInt(glassR[9 * i + j]);
                    door[i][j] = Integer.parseInt(doorR[9 * i + j]);
                    swich[i][j] = Integer.parseInt(swichR[9 * i + j]);
                    pillar[i][j] = Integer.parseInt(pillarR[9 * i + j]);
                }
            }
        } catch (NumberFormatException e) {
        }


        boolean res = deviceReader.is_ExternalStorageWriteable();
        if (res == true) {
            Log.w("TAGU", "storage available");
        }


    }


    private void updateDestination(String toValue) {
        Log.w("Dest", "Called " + toValue);

        switch (toValue) {
            case "Stall 1":
                destination = 1;
                break;
            case "Stall 2":
                destination = 3;
                break;
            case "Stall 3":
                destination = 8;
                break;
            case "Stall 4":
                destination = 11;
                break;
            case "Stall 5":
                destination = 16;
                break;
            case "Stall 6":
                destination = 18;
                break;
        }
    }

    private void updateSource(String fromValue) {

        Log.w("Source", "Called " + fromValue);

        switch (fromValue) {
            case "Stall 1":
                source = 1;
                break;
            case "Stall 2":
                source = 3;
                break;
            case "Stall 3":
                source = 8;
                break;
            case "Stall 4":
                source = 11;
                break;
            case "Stall 5":
                source = 16;
                break;
            case "Stall 6":
                source = 18;
                break;
        }
    }


    public void foundPosition(Node node) {
        Toast.makeText(getApplicationContext(), "Found " + node.getX() + " " + node.getY(), Toast.LENGTH_LONG).show();
        Log.w("MY_APP", Integer.toString(node.getX()) +" "+ Integer.toString(node.getY()));
    }

    public void compare(ArrayList<ScanResult> arrayList) {
        ScanResult scanGlass = null, scanDoor = null, scanSwich = null, scanPillar = null;

        try {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).SSID.equalsIgnoreCase("glass")) {
                    scanGlass = arrayList.get(i);
                    if (scanGlass == null) Log.w("MY_APP", "scanGlass null");
                    //  Log.w("MY_APP","Scan Glass found");
                } else if (arrayList.get(i).SSID.equalsIgnoreCase("door")) {
                    scanDoor = arrayList.get(i);
                    //  Log.w("MY_APP","Scan Door found");
                } else if (arrayList.get(i).SSID.equalsIgnoreCase("switch")) {
                    scanSwich = arrayList.get(i);
                    // Log.w("MY_APP","Scan Switch found");
                } else if (arrayList.get(i).SSID.equalsIgnoreCase("pillar")) {
                    scanPillar = arrayList.get(i);
                    //  Log.w("MY_APP","Scan Pillar found");
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
        if (scanGlass == null || scanDoor == null || scanPillar == null || scanSwich == null) {
            Toast.makeText(getApplicationContext(), "All wifi not available", Toast.LENGTH_SHORT).show();
        } else {
            scanGlass.level *= -1;
            scanDoor.level *= -1;
            scanPillar.level *= -1;
            scanSwich.level *= -1;

            if (scanPillar.level >= -50 && scanPillar.level <= -65 && scanDoor.level >= -50 && scanDoor.level <= -65) {
                if (scanSwich.level > scanGlass.level) {
                    if (scanPillar.level > scanDoor.level) foundPosition(new Node(355, 454));
                    else foundPosition(new Node(610, 454));
                } else {
                    if (scanPillar.level > scanDoor.level) foundPosition(new Node(355, 1070));
                    else foundPosition(new Node(610, 1070));
                }
            } else {
                if (scanPillar.level > scanDoor.level) {
                    if (scanSwich.level > scanGlass.level) foundPosition(new Node(110, 454));
                    else foundPosition(new Node(110, 1070));
                } else {
                    if (scanSwich.level > scanGlass.level) foundPosition(new Node(986, 454));
                    else foundPosition(new Node(986, 1070));
                }
            }

        /*

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                Log.w("MY_APP",glass[i][j] + " : " + scanGlass.level);
                Log.w("MY_APP",door[i][j] + " : " + scanDoor.level);
                Log.w("MY_APP",swich[i][j] + " : " + scanSwich.level);
                Log.w("MY_APP",pillar[i][j] + " : " + scanPillar.level);
                scanGlass.level*=-1;
                scanDoor.level*=-1;
                scanPillar.level*=-1;
                scanSwich.level*=-1;
                if (glass[i][j] >= scanGlass.level -5  && glass[i][j] <= scanGlass.level + 5 &&
                        door[i][j] >= scanDoor.level - 5 && door[i][j] <= scanDoor.level + 5 &&
                        swich[i][j] >= scanSwich.level - 5 && swich[i][j] <= scanSwich.level + 5 &&
                        pillar[i][j] >= scanPillar.level - 5 && pillar[i][j] <= scanPillar.level + 5) {
                    foundPosition(i, j);
                }
                else {Log.w("position", "not found");}
            }
        }
        */
        }
    }


        @Override
        public void onRequestPermissionsResult ( int requestCode, String[] permissions,
        int[] grantResults){
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
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Wifi Disabled, Enabling", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
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
            Toast.makeText(getApplicationContext(), "Total Networks found : " + s, Toast.LENGTH_LONG).show();
            if (wifiList.isEmpty()) {
                Toast.makeText(getApplicationContext(), "No network found", Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < wifiList.size(); i++) {
                    if (wifiList.get(i).SSID.equalsIgnoreCase("glass") || wifiList.get(i).SSID.equalsIgnoreCase("door") ||
                            wifiList.get(i).SSID.equalsIgnoreCase("pillar") || wifiList.get(i).SSID.equalsIgnoreCase("switch")) {
                        selectedWifi.add(wifiList.get(i));
                    }
                }
                if (selectedWifi.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No network found from d,p,s,g", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        for (int i = 0; i < selectedWifi.size(); i++) {
                            Toast.makeText(context, selectedWifi.get(i).SSID + " " + selectedWifi.get(i).level, Toast.LENGTH_LONG).show();
                        }
                    } catch (NoSuchElementException e) {
                        Log.w("TAGU", "EXCEPTION in selected wifi list");
                    }
                    compare(selectedWifi);
                }
            }
        }
    }
}