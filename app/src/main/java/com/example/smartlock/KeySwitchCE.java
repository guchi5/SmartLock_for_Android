package com.example.smartlock;

import android.os.Handler;

public class KeySwitchCE implements Runnable {
    private final Handler handler;
    private final MainActivity activity;
    private SmartLock smartLock;
    private final int cmd;
    public final static int UNLOCK = 0;
    public final static int LOCK = 1;
    public final static int TOGGLE = 2;

    KeySwitchCE(SmartLock smartLock, Handler handler, int cmd, MainActivity activity){
        this.smartLock = smartLock;
        this.handler = handler;
        this.cmd = cmd;
        this.activity = activity;
    }


    public static synchronized boolean unlock(SmartLock smartLock, MainActivity activity){
        return APIGateway.unlock(smartLock, activity);
    }

    public static synchronized boolean lock(SmartLock smartLock, MainActivity activity){
        return APIGateway.lock(smartLock, activity);
    }

    public static synchronized boolean toggle(SmartLock smartLock, MainActivity activity){
        return APIGateway.toggle(smartLock, activity);
    }

    @Override
    public void run() {
        switch (cmd){
            case UNLOCK:
                unlock(smartLock, this.activity);
                break;
            case LOCK:
                lock(smartLock, this.activity);
                break;
            case TOGGLE:
                toggle(smartLock, this.activity);
                break;
            default:
                System.out.println("コマンドエラー: 正しいコマンドを入力して下さい");
        }
    }
}

