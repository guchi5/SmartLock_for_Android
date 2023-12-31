package com.example.smartlock;

import java.sql.Time;
import java.util.List;

public class BeaconCE {
    private final MainActivity activity;
    private final BeaconDB db;
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

    public synchronized List<Beacon> getBeacons(){
        return db.getBeacons();
    }
    public synchronized Beacon getBeacon(String uuid, int major, int minor){
        return db.getBeacon(uuid, major, minor);
    }
    public static Beacon getBeaconInstance(String uuid, int major, int minor){
        Beacon beacon = new Beacon(uuid, major, minor);
        return beacon;
    }

    public synchronized boolean isRegistered(String uuid, int major, int minor){
        return db.isRegistered(uuid, major, minor);
    }

    public synchronized  boolean deleteBeacon(Beacon beacon){
        return db.deleteBeacon(beacon);
    }
    public synchronized  boolean updateBeacon(Beacon beacon){
        return db.updateBeacon(beacon);
    }
}
