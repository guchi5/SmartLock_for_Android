package com.example.smartlock;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        String format = (String) parent.getItemAtPosition(position);
        String[] data = format.split("\n");
        Beacon beacon = BeaconCE.getBeaconInstance(data[0].split("UUID:")[1], Integer.parseInt(data[1].split("major:")[1]), Integer.parseInt(data[2].split("minor:")[1]));
        deleteBeacon(beacon);
        return true;
    }

    public void updateListView(){
        List<Beacon> smartLocks = beaconCE.getBeacons();
        ArrayList<String> items = new ArrayList<>();

        for(Beacon beacon : smartLocks){
            String format = String.format("UUID:%s\nmajor:%d\nminor:%d",beacon.getUUID(), beacon.getMajor(), beacon.getMinor());
            items.add(format);
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }

    private void deleteBeacon(Beacon beacon){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setTitle("ビーコンを削除");
        builder.setMessage("UUID: "+beacon.getUUID()+"\nビーコンを削除しますか？");
        builder.setNeutralButton
                ("キャンセル",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int i)
                            {
                                // Nothing to do.
                            }
                        });

        builder.setPositiveButton("削除",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        beaconCE.deleteBeacon(beacon);
                        System.out.println("ビーコン削除");
                        updateListView();
                    }
                }
        );

        builder.create();
        builder.show();

    }
}
