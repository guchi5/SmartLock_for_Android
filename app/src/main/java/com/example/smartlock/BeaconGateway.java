package com.example.smartlock;

import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class BeaconGateway {
    private BeaconManager beaconManager;
    private MainActivity activity;
    protected static final String TAG = "MonitoringActivity";
    private Region my_region;
    private Collection<Beacon> beacon_list;

    BeaconGateway(MainActivity activity){
        this.activity = activity;
        this.beacon_list = new ArrayList<>();
        beaconManager = BeaconManager.getInstanceForApplication(this.activity);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        this.my_region = new Region("myMonitoringUniqueId", null, null, null);
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                beacon_list.clear();
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });
        beaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                    beacon_list = beacons;
                }
            }
        });

    }

    public Collection<Beacon> getBeacons(){
        return beacon_list;
    }

    public void start(){
        beaconManager.startMonitoring(my_region);
        beaconManager.startRangingBeacons(my_region);
    }

    public void stop(){
        beaconManager.stopMonitoring(my_region);
        beaconManager.stopRangingBeacons(my_region);
        beacon_list.clear();
    }

}
