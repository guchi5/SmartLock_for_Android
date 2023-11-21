package com.example.smartlock;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.core.os.HandlerCompat;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoUnlocker implements Runnable {
    private BeaconGateway beaconGateway;
    private MainActivity activity;
    private Switch sw;
    private final BeaconCE beaconCE;
    private Handler handler;
    private SmartLockCE smartLockCE;
    private KeySwitchCE keySwitchCE;
    AutoUnlocker(MainActivity activity, BeaconGateway beaconGateway, BeaconCE beaconCE, Handler handler, SmartLockCE smartLockCE){
        this.activity = activity;
        this.beaconGateway = beaconGateway;
        this.sw = this.activity.findViewById(R.id.beacon_switch);
        this.beaconCE = beaconCE;
        this.handler = handler;
        this.smartLockCE = smartLockCE;
    }


    @Override
    public void run() {
        boolean flag = false;
        beaconGateway.clear();
        sleep(3000);
        if(sw.isChecked()){
            List<Beacon> beacons = new ArrayList<>(beaconGateway.getBeacons());
            System.out.println("定期実行テスト "+beacons.size());
            for(Beacon beacon : beacons){
                boolean exist = beaconCE.isRegistered(beacon.getIdentifiers().get(0).toString(), beacon.getIdentifiers().get(1).toInt(), beacon.getIdentifiers().get(2).toInt());
                if(exist){
                    com.example.smartlock.Beacon registed_beacon = beaconCE.getBeacon(beacon.getIdentifiers().get(0).toString(), beacon.getIdentifiers().get(1).toInt(), beacon.getIdentifiers().get(2).toInt());
                    long nowTime = System.currentTimeMillis();
                    if(registed_beacon.getDate() == null){
                        registed_beacon.setDate(new Date(nowTime));
                    }
                    if(nowTime-registed_beacon.getDate().getTime()>(30*60)*1000){
                        registed_beacon.setDate(new Date(nowTime));
                        beaconCE.updateBeacon(registed_beacon);
                    }else{
                        registed_beacon.setDate(new Date(nowTime));
                        beaconCE.updateBeacon(registed_beacon);
                        continue;
                    }

                    System.out.println("発見！");
                    List<SmartLock> smartLocks =  smartLockCE.getSmartLocks();

                    for(SmartLock smartLock : smartLocks){
                        if(KeySwitchCE.unlock(smartLock)){
                            flag = true;
                        }else{
                            Toast.makeText(activity, (CharSequence) "開錠出来ませんでした", Toast.LENGTH_LONG).show();
                        }
                    }
                    if(flag){
                        break;
                    }

                }else{
                    System.out.println("未発見");
                }
            }
            handler.post(new PostExecutor(sw, flag, activity, this));
        }


    }
    public synchronized void sleep(long msec)
    {
        try
        {

            System.out.println("待機");
            wait(msec);
        }catch(InterruptedException e){}
    }

}
class PostExecutor implements Runnable {  // (4)
    private Switch sw;
    private boolean result;
    private final MainActivity activity;
    private AutoUnlocker autoUnlocker;
    PostExecutor(Switch sw, boolean result, MainActivity activity, AutoUnlocker autoUnlocker){
        this.sw = sw;
        this.result = result;
        this.activity = activity;
        this.autoUnlocker = autoUnlocker;
    }
    @Override
    public void run() {
        Log.i("Async-PostExecutor", "ここにUIスレッドで行いたい処理を記述する");  // (5)
        if(result){
            Toast.makeText(activity, (CharSequence) "開錠しました", Toast.LENGTH_LONG).show();
            //sw.setChecked(false);
        }
        if(sw.isChecked()){
            ExecutorService executorService  = Executors.newSingleThreadExecutor();
            executorService.submit(autoUnlocker);

        }
    }
}