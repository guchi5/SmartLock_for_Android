package com.example.smartlock;

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
        return false;
    }

    public void updateListView(){
        List<SmartLock> smartLocks = smartLockCE.getSmartLock();
        ArrayList<String> items = new ArrayList<>();

        for(SmartLock smartLock : smartLocks){
            items.add(smartLock.getName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }
}
