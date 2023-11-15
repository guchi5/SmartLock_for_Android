package com.example.smartlock;


import android.view.View;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

public class SmartLockRegister implements View.OnClickListener{
    MainActivity activity;
    PreviewView previewView;
    SmartLockRegister(MainActivity activity, PreviewView previewView){
        this.activity = activity;
        this.previewView = previewView;
    }

    private void startCamera(){
        ListenableFuture<ProcessCameraProvider> processCameraProviderListenableFuture = ProcessCameraProvider.getInstance(this.activity);
        processCameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try{
                    ProcessCameraProvider provider  = processCameraProviderListenableFuture.get();
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
