package com.example.smartlock;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.ByteArrayOutputStream;
public class SmartLockRegister implements View.OnClickListener{
    private MainActivity activity;
    private PreviewView previewView;
    private AlertDialog alertDialog;
    private ProcessCameraProvider provider;
    private BarcodeScanner barcodeScanner;
    private ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();
    private SmartLockCE smartLockCE;
    SmartLockRegister(MainActivity activity, SmartLockCE smartLockCE){
        this.activity = activity;
        this.smartLockCE = smartLockCE;
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                        .build();

        barcodeScanner = BarcodeScanning.getClient(options);
    }

    private void startCamera(){
        ListenableFuture<ProcessCameraProvider> cameraProvider  = ProcessCameraProvider.getInstance(this.activity);

        final View camearaView =
                activity.getLayoutInflater().inflate(R.layout.camera, null, false);
        previewView = camearaView.findViewById(R.id.viewFinder);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(camearaView);
        builder.setIcon(android.R.drawable.ic_menu_camera);
        builder.setOnDismissListener(
                new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        provider.unbindAll();
                    }
                }

        );
        builder.setTitle("QRコードをスキャン");
        builder.create();
        alertDialog = builder.show();
        cameraProvider .addListener(new Runnable() {
            @Override
            public void run() {
                try{
                    provider  = cameraProvider.get();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());
                    ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build();
                    CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
                    imageAnalysis.setAnalyzer(cameraExecutor, new ImageAnalysis.Analyzer() {
                        @Override
                        @ExperimentalGetImage
                        public void analyze(ImageProxy image) {
                            com.google.mlkit.vision.common.InputImage inputImage = com.google.mlkit.vision.common.InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());
                            barcodeScanner.process(inputImage)
                                    .addOnSuccessListener(barcodes -> {
                                        for (Barcode barcode : barcodes) {
                                            if (barcode.getFormat() == Barcode.FORMAT_QR_CODE) {
                                                String qrText = barcode.getRawValue();
                                                provider.unbindAll();
                                                alertDialog.dismiss();
                                                // QRコードが見つかった場合の処理を行う
                                                System.out.println("QRコード："+qrText);
                                                String[] encode_qr = new String[0];
                                                try {
                                                    encode_qr = handleQRCode(qrText);
                                                    if(encode_qr != null){
                                                        if(smartLockCE.addSmartLock(smartLockCE.createSmartLock(encode_qr))){
                                                            activity.updateSmartLockView();
                                                            Toast.makeText(activity, (CharSequence) "追加完了", Toast.LENGTH_LONG).show();
                                                        }else{
                                                            Toast.makeText(activity, (CharSequence) "すでに追加済みです", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                } catch (UnsupportedEncodingException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        }
                                        image.close(); // 画像をクローズしてリソースを解放
                                    })
                                    .addOnFailureListener(e -> {
                                        // エラー処理を行う
                                        image.close(); // 画像をクローズしてリソースを解放
                                    });
                        }
                    });
                    provider.bindToLifecycle((LifecycleOwner)activity, cameraSelector, preview, imageAnalysis);

                }catch (Exception e){

                }
            }
        }, ContextCompat.getMainExecutor(this.activity)
        );

    }

    private String[] handleQRCode(String url) throws UnsupportedEncodingException {
        try{
            String[] query = url.split("&");
            String name = URLDecoder.decode(query[3].split("=")[1], "UTF-8");
            byte[] sk =  Base64.decode(query[1].split("=")[1], Base64.DEFAULT);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(sk, 83, 16);
            byte[] byte_uuid = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.reset();
            byteArrayOutputStream.write(sk, 1, 16);
            byte[] byte_secret_key = byteArrayOutputStream.toByteArray();
            String uuid = new StringBuilder(hex(byte_uuid)).insert(8, "-").insert(13, "-").insert(18, "-").insert(23, "-").toString().toUpperCase();
            String secret_key = hex(byte_secret_key);
            System.out.println("uuid:："+uuid);
            System.out.println("secret_key:"+secret_key);
            return new String[] {uuid, secret_key, name};
        }catch (Exception e){
            Toast.makeText(activity, (CharSequence) "Error: 対応していないQRコードです", Toast.LENGTH_LONG).show();
            return null;
        }

    }
    private String hex(byte[] data){
        byte[] decode_byte = Arrays.copyOf(data, data.length);
        StringBuilder sb = new StringBuilder();
        for (byte b : decode_byte) {
            sb.append(String.format("%02x", b));
        }
        String ret = sb.toString();
        return ret;
    }
    @Override
    public void onClick(View v) {
        startCamera();
    }
}
