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

    private static final String TABLE_BEACONS = "beacons";
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

    public ContentValues toSmartLockValue(SmartLock smartLock){
        ContentValues values = new ContentValues();
        values.put(FIELD_SMART_LOCK_UUID, smartLock.getUUID());
        values.put(FIELD_SMART_LOCK_SECRET_KEY, smartLock.getSecretKey());
        values.put(FIELD_SMART_LOCK_NAME, smartLock.getName());

        return values;
    }
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
}
