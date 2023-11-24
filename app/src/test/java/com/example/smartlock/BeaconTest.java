package com.example.smartlock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Date;

public class BeaconTest {

    @Test
    public void getUUID(){
        Beacon beacon = new Beacon("test", 1, 2);
        assertEquals("test", beacon.getUUID());
    }
    public void getMajor(){
        Beacon beacon = new Beacon("test", 1, 2);
        assertEquals(1, beacon.getUUID());
    }
    public void getMinor(){
        Beacon beacon = new Beacon("test", 1, 2);
        assertEquals(2, beacon.getUUID());
    }

    public void getDate(){
        Beacon beacon = new Beacon("test", 1, 2);
        beacon.setDate(new Date());
        assertEquals(2, beacon.getDate());
    }

}
