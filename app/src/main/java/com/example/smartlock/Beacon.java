package com.example.smartlock;

import java.sql.Time;
import java.util.Date;

public class Beacon {
    private String uuid;
    private int major;
    private int minor;
    private Date date;
    Beacon(String uuid, int major, int minor){
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.date = null;
    }

    public String getUUID() {
        return uuid;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
