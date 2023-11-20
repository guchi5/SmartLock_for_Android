package com.example.smartlock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmartLockLister implements AdapterView.OnItemLongClickListener {
    private MainActivity activity;
    private ListView listView;
    private SmartLockCE smartLockCE;
    SmartLockLister(MainActivity activity, ListView listView, SmartLockCE smartLockCE){
        this.activity = activity;
        this.listView = listView;
        this.smartLockCE = smartLockCE;
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        SmartLock smartLock = smartLockCE.getSmartLockAt(position);
        deleteSmartLock(smartLock);
        return false;
    }

    public void updateListView(){
        List<SmartLock> smartLocks = smartLockCE.getSmartLocks();
        ArrayList<String> items = new ArrayList<>();

        for(SmartLock smartLock : smartLocks){
            items.add(smartLock.getName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }

    private void deleteSmartLock(SmartLock smartLock){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setTitle("スマートロックを削除");
        builder.setMessage("Name: "+smartLock.getName()+"\nスマートロックを削除しますか？");
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
                        smartLockCE.deleteSmartLock(smartLock);
                        System.out.println("スマートロック削除");
                        updateListView();
                    }
                }
        );

        builder.create();
        builder.show();

    }
}
