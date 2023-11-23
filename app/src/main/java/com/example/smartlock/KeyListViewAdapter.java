package com.example.smartlock;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.core.os.HandlerCompat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyListViewAdapter extends SimpleAdapter {
    private LayoutInflater inflater;
    private List<? extends Map<String, ?>> listData;
    private Context context;
    private SmartLockCE smartLockCE;
    // 各行が保持するデータ保持クラス
    public class ViewHolder {
        TextView text1;

    }

    public KeyListViewAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, SmartLockCE smartLockCE) {
        super(context, data, resource, from, to);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listData = data;
        this.context = context;
        this.smartLockCE = smartLockCE;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        // ビューを受け取る
        View view = convertView;

        if (view == null) {
            // 画面起動時にViewHolderを作成する
            view = inflater.inflate(R.layout.row, parent, false);

            holder = new ViewHolder();
            holder.text1 = (TextView) view.findViewById(R.id.text1);

            view.setTag(holder);
        } else {
            // 行選択時などは既に作成されているものを取得する
            holder = (ViewHolder) view.getTag();
        }

        String text1 = ((HashMap<?, ?>) listData.get(position)).get("text1").toString();
        holder.text1.setText(text1);

        // セル上にあるボタンの処理
        Button btn = (Button) view.findViewById(R.id.rowbutton);
        btn.setTag(position);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Looper mainLooper = Looper.getMainLooper();  // (1)
                Handler handler = HandlerCompat.createAsync(mainLooper);  // (2)
                SmartLock smartLock = smartLockCE.getSmartLockAt(position);
                KeySwitchCE keySwitchCE = new KeySwitchCE(smartLock, handler, KeySwitchCE.TOGGLE, (MainActivity) context);
                ExecutorService executorService  = Executors.newSingleThreadExecutor();
                executorService.submit(keySwitchCE);
                System.out.println("押された");
            }
        });

        return view;
    }}
