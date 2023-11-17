package com.example.smartlock;

public class SmartLock {
    private String uuid;
    private String secret_key;
    private String name;
    SmartLock(String uuid, String secret_key, String name){
        this.uuid = uuid;
        this.secret_key = secret_key;
        this.name = name;
    }
    SmartLock(){

    }

    public String getUUID(){
        return this.uuid;
    }

    public String getSecretKey(){
        return this.secret_key;
    }

    public String getName(){
        return this.name;
    }

    public void setUUID(String uuid){
        this.uuid = uuid;
    }

    public void setSecretKey(String secret_key){
        this.secret_key = secret_key;
    }

    public void setName(String name){
        this.name = name;
    }
}
