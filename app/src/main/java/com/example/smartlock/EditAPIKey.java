package com.example.smartlock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditAPIKey implements View.OnClickListener{

    private Button edit_api_key;
    private MainActivity activity;
    private SmartLockCE smartLockCE;
    EditAPIKey(MainActivity activity, SmartLockCE smartLockCE){
        this.activity = activity;
        this.smartLockCE = smartLockCE;
        this.edit_api_key = activity.findViewById(R.id.api_key_setting);

    }
    @Override
    public void onClick(View v) {
        final View api_key_setting =
                activity.getLayoutInflater().inflate(R.layout.api_key_setting, null, false);
        EditText editText = api_key_setting.findViewById(R.id.edit_api_key);
        String api_key = smartLockCE.getAPIKey();
        if(api_key == null){
            api_key = "38kpmfOoGh1WwQJXcoiGd4ieGVCasblD66mXjT5g";
        }
        editText.setText(api_key);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(api_key_setting);
        builder.setIcon(android.R.drawable.ic_search_category_default);
        builder.setPositiveButton("変更", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String api_key = editText.getText().toString();
                if(smartLockCE.updateAPIKey(api_key)){
                    Toast.makeText(activity, (CharSequence) "変更完了", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create();
        builder.show();
    }
}
