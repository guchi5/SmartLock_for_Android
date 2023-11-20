package com.example.smartlock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class DatabaseCE extends SQLiteOpenHelper implements SmartLockDB, BeaconDB {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Main.db";

    private static final String	LOGTAG = "DatabaseCE";
    public DatabaseCE(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("CREATE TABLE %s(" +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s TEXT," +
                        "PRIMARY KEY(%s, %s))", TABLE_SMART_LOCKS,
                FIELD_SMART_LOCK_UUID,
                FIELD_SMART_LOCK_SECRET_KEY,
                FIELD_SMART_LOCK_NAME,
                FIELD_SMART_LOCK_UUID,
                FIELD_SMART_LOCK_SECRET_KEY));

        db.execSQL(String.format("CREATE TABLE %s(" +
                        "%s TEXT NOT NULL," +
                        "%s INTEGER NOT NULL," +
                        "%s INTEGER NOT NULL," +
                        "PRIMARY KEY(%s, %s, %s))",
                TABLE_BEACONS,
                FIELD_BEACON_UUID,
                FIELD_BEACON_MAJOR,
                FIELD_BEACON_MINOR,
                FIELD_BEACON_UUID,
                FIELD_BEACON_MAJOR,
                FIELD_BEACON_MINOR));


    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        if(oldVersion != newVersion){
            db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_SMART_LOCKS));
            db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_BEACONS));
            onCreate(db);
        }
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private ContentValues toSmartLockValue(SmartLock smartLock){
        ContentValues values = new ContentValues();
        values.put(FIELD_SMART_LOCK_UUID, smartLock.getUUID());
        values.put(FIELD_SMART_LOCK_SECRET_KEY, smartLock.getSecretKey());
        values.put(FIELD_SMART_LOCK_NAME, smartLock.getName());

        return values;
    }

    @Override
    public boolean addSmartLock(SmartLock smartLock){
        Log.d(LOGTAG, "INSERT" + smartLock);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = -1;
        try{
            ContentValues values = toSmartLockValue(smartLock);
            id = db.insert(TABLE_SMART_LOCKS, null, values);
        }finally {
            db.close();
        }
        if(id!=-1){
            return true;
        }else{
            return false;
        }
    }
    private SmartLock toSmartLock(Cursor cursor)
    {
        SmartLock smartLock = new SmartLock();
        smartLock.setUUID(cursor.getString(0));
        smartLock.setSecretKey(cursor.getString(1));
        smartLock.setName(cursor.getString(2));
        return smartLock;
    }

    @Override
    public List<SmartLock> getSmartLocks(){
        List<SmartLock> smartLocks = new LinkedList<>();
        String query = String.format("SELECT * FROM %s", TABLE_SMART_LOCKS);
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            Cursor cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()) {
                SmartLock smartLock = toSmartLock(cursor);
                smartLocks.add(smartLock);
            }
        }finally {
            db.close();
        }
        return smartLocks;
    }

    @Override
    public boolean addBeacon(Beacon beacon) {
        Log.d(LOGTAG, "INSERT" + beacon);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = -1;
        try{
            ContentValues values = toBeaconValue(beacon);
            id = db.insert(TABLE_BEACONS, null, values);
        }finally {
            db.close();
        }
        if(id!=-1){
            return true;
        }else{
            return false;
        }
    }

    private ContentValues toBeaconValue(Beacon beacon) {
        ContentValues values = new ContentValues();
        values.put(FIELD_BEACON_UUID, beacon.getUUID());
        values.put(FIELD_BEACON_MAJOR, beacon.getMajor());
        values.put(FIELD_BEACON_MINOR, beacon.getMinor());

        return values;
    }

    private Beacon toBeacon(Cursor cursor) {
        return BeaconCE.getBeaconInstance(cursor.getString(0), cursor.getInt(1), cursor.getInt(2));
    }

    @Override
    public List<Beacon> getBeacon() {
        List<Beacon> beacons = new LinkedList<>();
        String query = String.format("SELECT * FROM %s", TABLE_BEACONS);
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            Cursor cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()) {
                Beacon beacon = toBeacon(cursor);
                beacons.add(beacon);
            }
        }finally {
            db.close();
        }
        return beacons;

    }

    @Override
    public boolean isRegistered(String uuid, int major, int minor) {
        boolean existence = false;
        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s='%s' and %s=%d and %s=%d",
                TABLE_BEACONS,
                FIELD_BEACON_UUID,
                uuid,
                FIELD_BEACON_MAJOR,
                major,
                FIELD_BEACON_MINOR,
                minor
        );
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToNext();
            if(cursor.getInt(0) >= 1){
                existence = true;
            }

        }finally {
            db.close();
            return existence;
        }

    }

    @Override
    public boolean deleteBeacon(Beacon beacon) {
        Log.d(LOGTAG, "DELETE" + beacon);
        SQLiteDatabase db = this.getWritableDatabase();
        long id;
        try{
            id = db.delete(TABLE_BEACONS,
                    FIELD_BEACON_UUID+"= ? AND "+FIELD_BEACON_MAJOR+"=? AND "+FIELD_BEACON_MINOR+"=?"
                    , new String[]{beacon.getUUID(),
                            String.valueOf(beacon.getMajor()),
                            String.valueOf(beacon.getMinor())});
        }finally {
            db.close();
        }
        if(id!=-1){
            return true;
        }else{
            return false;
        }

    }
}
