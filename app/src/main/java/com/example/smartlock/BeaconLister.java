package com.example.smartlock;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BeaconLister implements AdapterView.OnItemLongClickListener {
    private MainActivity activity;
    private ListView listView;
    private BeaconCE beaconCE;
    BeaconLister(MainActivity activity, ListView listView, BeaconCE beaconCE){
        this.activity = activity;
        this.listView = listView;
        this.beaconCE = beaconCE;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    public void updateListView(){
        List<Beacon> smartLocks = beaconCE.getBeacon();
        ArrayList<String> items = new ArrayList<>();

        for(Beacon beacon : smartLocks){
            items.add(beacon.getUUID());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }
}
