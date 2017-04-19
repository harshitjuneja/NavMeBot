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
    DeviceWriter deviceWriter;
    Route route;
    Spinner fromSpinner;
    int destination = 0;
    int source = 0;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MyMap) findViewById(R.id.canvas);
        tb = (Toolbar) findViewById(R.id.toolbar);
        goDirect= (ImageButton) findViewById(R.id.navigatebutton);

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

        route = new Route();

        goDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                route.routing(graph,source,destination);
            }
        });

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiAccess = new WifiAccess();


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

        /*boolean res = deviceReader.is_ExternalStorageWriteable();
        if (res == true) {
            Log.w("TAGU", "storage available");
        }*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED |
                        checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED |
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, 0x12345);
        } else {
            getSignalStrength();
        }
    }


    private void updateDestination(String toValue) {
        Log.w("MY_APP", "Called " + toValue);

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

        Log.w("MY_APP", "Called " + fromValue);

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




    public void foundPosition(int i, int j) {
        Log.w(Integer.toString(i), Integer.toString(j));
    }

    public void compare(ArrayList<ScanResult> arrayList) {
        ScanResult scanGlass = null, scanDoor = null, scanSwich = null, scanPillar = null;
        int glass[][] = new int[7][9];
        int door[][] = new int[7][9];
        int swich[][] = new int[7][9];
        int pillar[][] = new int[7][9];

        String glassR[] = glassReadings.split(" ");
        String doorR[] = doorReadings.split(" ");
        String swichR[] = switchReadings.split(" ");
        String pillarR[] = pillarReadings.split(" ");

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

        try {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).SSID.equalsIgnoreCase("glass")) {
                    scanGlass = arrayList.get(i);
                } else if (arrayList.get(i).SSID.equalsIgnoreCase("door")) {
                    scanDoor = arrayList.get(i);
                } else if (arrayList.get(i).SSID.equalsIgnoreCase("switch")) {
                    scanSwich = arrayList.get(i);
                } else if (arrayList.get(i).SSID.equalsIgnoreCase("pillar")) {
                    scanPillar = arrayList.get(i);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (glass[i][j] >= scanGlass.level - 3 && glass[i][j] <= scanGlass.level + 3 &&
                        door[i][j] >= scanDoor.level - 3 && door[i][j] <= scanDoor.level + 3 &&
                        swich[i][j] >= scanSwich.level - 3 && swich[i][j] <= scanSwich.level + 3 &&
                        pillar[i][j] >= scanPillar.level - 3 && pillar[i][j] >= scanPillar.level + 3)
                    foundPosition(i, j);
                else Log.w("position", "not found");
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