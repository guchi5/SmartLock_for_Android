package com.example.smartlock;

import android.os.Handler;

public class KeySwitchCE implements Runnable {
    private final Handler handler;
    private SmartLock smartLock;
    private final int cmd;
    public final static int UNLOCK = 0;
    public final static int LOCK = 1;
    public final static int TOGGLE = 2;

    KeySwitchCE(SmartLock smartLock, Handler handler, int cmd){
        this.smartLock = smartLock;
        this.handler = handler;
        this.cmd = cmd;
    }


    public static synchronized boolean unlock(SmartLock smartLock){
        return APIGateway.unlock(smartLock);
    }

    public static synchronized boolean lock(SmartLock smartLock){
        return APIGateway.lock(smartLock);
    }

    public static synchronized boolean toggle(SmartLock smartLock){
        return APIGateway.toggle(smartLock);
    }

    @Override
    public void run() {
        switch (cmd){
            case UNLOCK:
                unlock(smartLock);
                break;
            case LOCK:
                lock(smartLock);
                break;
            case TOGGLE:
                toggle(smartLock);
                break;
            default:
                System.out.println("コマンドエラー: 正しいコマンドを入力して下さい");
        }
    }
}

