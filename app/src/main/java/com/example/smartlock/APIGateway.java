package com.example.smartlock;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class APIGateway {
    private final static String URL_FORMAT = "https://app.candyhouse.co/api/sesame2/%s/cmd";
    private final static int LOCK_CMD =  82;
    private final static int UNLOCK_CMD =  83;
    private final static int TOGGLE_CND = 88;
    public static boolean unlock(){
        return false;
    }

    public static boolean lock(){
        return false;
    }

    private void createRequest(int cmd) {
        try{
            URL url = new URL(String.format(URL_FORMAT,"UUID"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
