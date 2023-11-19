package com.example.smartlock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    protected static final String TAG = "MonitoringActivity";
    private BeaconGateway b_gateway;
    private SmartLockRegister add_smart_lock;
    private SmartLockLister smartLockLister;
    private SmartLockCE smartLockCE;
    private BeaconLister beaconLister;
    private BeaconCE beaconCE;
    private BeaconRegister beaconRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location and camera access");
                builder.setMessage("位置情報とカメラへのアクセスを許可してください");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA},
                                PERMISSION_REQUEST_COARSE_LOCATION
                        );
                    }
                });
                builder.show();
            }
        }

        b_gateway = new BeaconGateway(this);
        Switch sw = findViewById(R.id.beacon_switch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    b_gateway.start();
                } else {
                    // The toggle is disabled
                    b_gateway.stop();
                }
            }
        });
        this.smartLockCE = new SmartLockCE(this);
        add_smart_lock = new SmartLockRegister(this, this.smartLockCE);
        Button addSmartLockBtn =findViewById(R.id.add_smartlock);
        addSmartLockBtn.setOnClickListener(add_smart_lock);
        ListView smartLockList = (ListView)findViewById(R.id.smart_lock_list);
        this.smartLockLister = new SmartLockLister(this, smartLockList, this.smartLockCE);
        smartLockList.setOnItemLongClickListener(this.smartLockLister);
        this.beaconCE = new BeaconCE(this);
        ListView beaconList = (ListView)findViewById(R.id.beacon_list);
        this.beaconLister = new BeaconLister(this, beaconList, this.beaconCE);
        beaconList.setOnItemLongClickListener(this.beaconLister);
        this.beaconRegister = new BeaconRegister(this, this.beaconCE, this.b_gateway);
        Button addBeacon = findViewById(R.id.add_beacon);
        addBeacon.setOnClickListener(this.beaconRegister);
        updateBeaconView();
        updateSmartLockView();
    }
    public void updateSmartLockView(){
        smartLockLister.updateListView();
    }

    public void updateBeaconView(){
        beaconLister.updateListView();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

}