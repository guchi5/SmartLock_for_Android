package com.example.smartlock;

import java.util.List;

public class BeaconCE {
    private MainActivity activity;
    private BeaconDB db;
    BeaconCE(MainActivity activity){
        this.activity = activity;
        this.db = new DatabaseCE(this.activity);
    }

    public synchronized boolean addBeacon(Beacon beacon){
        if(beacon != null){
            return db.addBeacon(beacon);
        }else{
            return false;
        }
    }

    public synchronized List<Beacon> getBeacon(){
        return db.getBeacon();
    }

    public Beacon createBeacon(String uuid, int major, int minor){
        Beacon beacon = new Beacon(uuid, major, minor);
        return beacon;
    }
}
