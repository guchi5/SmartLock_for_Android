package com.example.smartlock;

import java.util.List;

public interface SmartLockDB {
    static final String FIELD_SMART_LOCK_UUID = "uuid";
    static final String FIELD_SMART_LOCK_SECRET_KEY = "secret_key";
    static final String FIELD_SMART_LOCK_NAME = "name";
    static final String TABLE_SMART_LOCKS = "smartlocks";

    static final String TABLE_API_KEY = "api_key";
    static final String FIELD_API_KEY = "api_key";

    public boolean addSmartLock(SmartLock smartLock);
    public List<SmartLock> getSmartLocks();
    boolean deleteSmartLock(SmartLock smartLock);
    SmartLock getSmartLockAt(int position);

    boolean addAPIKey(String api_key);
    boolean updateAPIKey(String api_key);
    String getAPIKey();
    int countAPIKey();
}
