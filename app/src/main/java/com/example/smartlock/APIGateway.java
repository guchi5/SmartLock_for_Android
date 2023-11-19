package com.example.smartlock;
import android.util.Base64;

import org.spongycastle.crypto.BlockCipher;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.macs.CMac;
import org.spongycastle.crypto.params.KeyParameter;

import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;


public class APIGateway {
    private final static String URL_FORMAT = "https://app.candyhouse.co/api/sesame2/%s/cmd";
    private final static int LOCK_CMD =  82;
    private final static int UNLOCK_CMD =  83;
    private final static int TOGGLE_CND = 88;
    public static boolean unlock(SmartLock smartLock){
        return executeRequest(UNLOCK_CMD, smartLock);
    }

    public static boolean lock(SmartLock smartLock){
        return executeRequest(LOCK_CMD, smartLock);
    }

    public static boolean toggle(SmartLock smartLock){
        return executeRequest(TOGGLE_CND, smartLock);
    }
    private static boolean executeRequest(int cmd, SmartLock smartLock) {
        try{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String base64_history = Base64.encodeToString("Android_SmartLock".getBytes(), Base64.DEFAULT).replace("\n", "");
                URL url = new URL(String.format(URL_FORMAT, smartLock.getUUID()));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setInstanceFollowRedirects(false);
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("x-api-key", "38kpmfOoGh1WwQJXcoiGd4ieGVCasblD66mXjT5g");
                connection.setDoOutput(true);

                String sign = generateRandomTag(smartLock.getSecretKey()).toLowerCase();
                String json = String.format("{\"cmd\": %d, \"history\": \"%s\", \"sign\": \"%s\"}", cmd, base64_history, sign);
                PrintStream ps = new PrintStream(connection.getOutputStream());
                ps.print(json);
                ps.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Response: " + responseCode);
                } else {
                    System.out.println("POST request failed"+responseCode);
                }
                connection.disconnect();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static String generateRandomTag(String secret_key){
        // 1. timestamp  (SECONDS SINCE JAN 01 1970. (UTC))  // 1621854456905
        long timestamp = new Date().getTime() / 1000;
        // 2. timestamp to uint32  (little endian)   //f888ab60
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(timestamp);
        // 3. remove most-significant byte    //0x88ab60
        byte[] message = Arrays.copyOfRange(buffer.array(), 1,4);
        return getCMAC(parseHexStr2Byte(secret_key), message).replace(" ","");
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0; i < hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte)(high * 16 + low);
        }
        return result;
    }

    public static String getCMAC(byte[] secretKey, byte[] msg) {
        CipherParameters params = new KeyParameter(secretKey);
        BlockCipher aes = new AESEngine();
        CMac mac = new CMac(aes);
        mac.init(params);
        mac.update(msg, 0, msg.length);
        byte[] out = new byte[mac.getMacSize()];
        mac.doFinal(out, 0);

        StringBuilder s19 = new StringBuilder();
        for (byte b : out) {
            s19.append(String.format("%02x ", b));
        }
        return s19.toString();
    }

}
