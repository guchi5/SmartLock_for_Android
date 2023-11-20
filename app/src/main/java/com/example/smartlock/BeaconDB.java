package com.example.smartlock;

import java.util.List;

public interface BeaconDB {
    static final String TABLE_BEACONS = "beacons";
    static final String FIELD_BEACON_UUID = "uuid";
    static final String FIELD_BEACON_MAJOR = "major";
    static final String FIELD_BEACON_MINOR = "minor";


    boolean addBeacon(Beacon beacon);
    List<Beacon> getBeacon();

    boolean isRegistered(String uuid, int major, int minor);

    boolean deleteBeacon(Beacon beacon);
}
