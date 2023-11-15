package com.example.smartlock;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

public class SmartLockLister implements AdapterView.OnItemLongClickListener {
    private MainActivity activity;
    private ListView listView;
    SmartLockLister(MainActivity activity, ListView listView){
        this.activity = activity;
        this.listView = listView;
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    public void updateListView(String smartLock){
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, Arrays.asList(smartLock));
        listView.setAdapter(adapter);
    }
}
