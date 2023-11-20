package com.example.smartlock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BeaconRegister implements View.OnClickListener{
    private MainActivity activity;
    private BeaconCE beaconCE;
    private BeaconGateway beaconGateway;
    private CountDownTimer countDownTimer;
    private AlertDialog alertDialog;
    private ArrayAdapter<String> adapter;

    BeaconRegister(MainActivity activity, BeaconCE beaconCE, BeaconGateway beaconGateway){
        this.activity = activity;
        this.beaconCE = beaconCE;
        this.beaconGateway = beaconGateway;
    }


    @Override
    public void onClick(View v) {
        Switch sw = activity.findViewById(R.id.beacon_switch);
        sw.setChecked(false);
        final View beacon_list =
                activity.getLayoutInflater().inflate(R.layout.beacon_list, null, false);
        ListView beacon_list_view = beacon_list.findViewById(R.id.beacon_list);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(beacon_list);
        builder.setOnDismissListener(
                new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        countDownTimer.cancel();
                        beaconGateway.stop();
                    }
                }

        );
        builder.setTitle("ビーコン一覧");
        alertDialog = builder.create();
        alertDialog.show();
        beacon_list_view.setOnItemClickListener(new  AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String format = (String) parent.getItemAtPosition(position);
                String[] data = format.split("\n");
                System.out.println(data.length);
                try{
                    com.example.smartlock.Beacon beacon = BeaconCE.getBeaconInstance(data[0].split("UUID:")[1], Integer.parseInt(data[1].split("major:")[1]), Integer.parseInt(data[2].split("minor:")[1]));
                    System.out.println("UUID"+beacon.getUUID());
                    if(beaconCE.addBeacon(beacon)){
                        activity.updateBeaconView();
                        Toast.makeText(activity, (CharSequence) parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(activity, (CharSequence) "既に登録済みです", Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(activity, (CharSequence) "登録できません", Toast.LENGTH_LONG).show();
                }

            }
        });
        beaconGateway.start();
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                List<String> items = new ArrayList<>();
                Iterator<Beacon> beacons = beaconGateway.getBeacons().iterator();
                while(beacons.hasNext()){
                    List<Identifier> beacon = beacons.next().getIdentifiers();
                    String format = String.format("UUID:%s\nmajor:%s\nminor:%s",beacon.get(0), beacon.get(1), beacon.get(2));
                    items.add(format);
                }
                System.out.println("ビーコン一覧："+items);
                adapter =
                        new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, items);
                beacon_list_view.setAdapter(adapter);
            }

            @Override
            public void onFinish() {
                countDownTimer.start();

            }
        }.start();

    }
}
