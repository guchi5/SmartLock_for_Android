package com.example.smartlock;

import android.os.Handler;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;

import java.util.Iterator;
import java.util.List;

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
        while(true){
            System.out.println("定期実行テスト");
            Iterator<Beacon> beacons = beaconGateway.getBeacons().iterator();
            while(beacons.hasNext()){
                List<Identifier> beacon = beacons.next().getIdentifiers();
                boolean exist = beaconCE.isRegistered(beacon.get(0).toString(), beacon.get(1).toInt(), beacon.get(2).toInt());
                if(exist){
                    System.out.println("発見！");
                    List<SmartLock> smartLocks =  smartLockCE.getSmartLock();
                    beaconGateway.stop();
                    boolean flag = false;
                    for(SmartLock smartLock : smartLocks){
                        if(KeySwitchCE.unlock(smartLock)){
                            flag = true;
                        }
                    }
                    handler.post(new PostExecutor(sw, flag, activity));
                }else{
                    System.out.println("未発見");
                }
            }

            if(!sw.isChecked()){
                break;
            }
        }
    }
}
class PostExecutor implements Runnable {  // (4)
    private Switch sw;
    private boolean result;
    private final MainActivity activity;
    PostExecutor(Switch sw, boolean result, MainActivity activity){
        this.sw = sw;
        this.result = result;
        this.activity = activity;
    }
    @Override
    public void run() {
        Log.i("Async-PostExecutor", "ここにUIスレッドで行いたい処理を記述する");  // (5)
        if(result){
            Toast.makeText(activity, (CharSequence) "開錠しました", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(activity, (CharSequence) "開錠出来ませんでした", Toast.LENGTH_LONG).show();
        }
        sw.setChecked(false);
    }
}