package com.example.smartlock;

import android.view.View;

public class BeaconRegister implements View.OnClickListener{
    private MainActivity activity;
    private BeaconCE beaconCE;
    BeaconRegister(MainActivity activity, BeaconCE beaconCE){
        this.activity = activity;
        this.beaconCE = beaconCE;
    }

    @Override
    public void onClick(View v) {

    }
}
