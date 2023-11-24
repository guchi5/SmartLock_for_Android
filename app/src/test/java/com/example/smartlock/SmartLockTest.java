package com.example.smartlock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SmartLockTest {
    @Test
    public void getUUID(){
        SmartLock smartLock = new SmartLock();
        assertEquals(null, smartLock.getUUID());
    }

    @Test
    public void getSecretKey(){
        SmartLock smartLock = new SmartLock();
        assertEquals(null, smartLock.getSecretKey());
    }

    @Test
    public void getName(){
        SmartLock smartLock = new SmartLock();
        assertEquals(null, smartLock.getName());
    }
}
