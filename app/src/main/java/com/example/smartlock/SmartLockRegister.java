package com.example.smartlock;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

public class SmartLockRegister implements View.OnClickListener{
    private MainActivity activity;
    private PreviewView previewView;
    private AlertDialog alertDialog;
    private ProcessCameraProvider provider;
    SmartLockRegister(MainActivity activity){
        this.activity = activity;
    }

    private void startCamera(){
        ListenableFuture<ProcessCameraProvider> processCameraProviderListenableFuture = ProcessCameraProvider.getInstance(this.activity);

        final View camearaView =
                activity.getLayoutInflater().inflate(R.layout.camera, null, false);
        previewView = camearaView.findViewById(R.id.viewFinder);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(camearaView);
        builder.setOnDismissListener(
                new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        provider.unbindAll();
                    }
                }

        );
        builder.create();
        alertDialog = builder.show();

        processCameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try{
                    provider  = processCameraProviderListenableFuture.get();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());

                    CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
                    provider.bindToLifecycle((LifecycleOwner)activity, cameraSelector, preview);
                }catch (Exception e){

                }
            }
        }, ContextCompat.getMainExecutor(this.activity)
        );

    }

    @Override
    public void onClick(View v) {
        startCamera();
    }
}
