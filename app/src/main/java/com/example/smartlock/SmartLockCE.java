package com.example.smartlock;

import java.util.List;

public class SmartLockCE {
    private MainActivity activity;
    private SmartLockDB db;
    SmartLockCE(MainActivity activity){
        this.activity = activity;
        db = new DatabaseCE(this.activity);
    }

    public synchronized boolean addSmartLock(SmartLock smartLock){
        if(smartLock != null){
            return db.addSmartLock(smartLock);
        }
        return false;

    }


    public synchronized List<SmartLock> getSmartLock() {
        return db.getSmartLocks();
    }

    public SmartLock createSmartLock(String[] data){
        SmartLock smartLock = new SmartLock();
        smartLock.setUUID(data[0]);
        smartLock.setSecretKey(data[1]);
        smartLock.setName(data[2]);
        return smartLock;
    }

}
