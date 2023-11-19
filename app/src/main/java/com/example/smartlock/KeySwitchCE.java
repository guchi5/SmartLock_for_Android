package com.example.smartlock;

import android.os.Handler;

public class KeySwitchCE{
    private SmartLock smartLock;
    KeySwitchCE(SmartLock smartLock){
        this.smartLock = smartLock;
    }


    public static boolean unlock(SmartLock smartLock){
        return APIGateway.unlock(smartLock);
    }

    public boolean lock(SmartLock smartLock){
        return APIGateway.lock(smartLock);
    }

    public boolean toggle(SmartLock smartLock){
        return APIGateway.toggle(smartLock);
    }
}
