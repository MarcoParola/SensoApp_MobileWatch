package com.example.shared_mobile_watch;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataSensor implements Serializable {
    private int sensorType;
    private String value0;
    private String value1;
    private String value2;
    private String timestamp;

    public int getSensorType() {
        return sensorType;
    }
    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }

    public String getValue0() {
        return value0;
    }
    public void setValue0(String value0) {
        this.value0 = value0;
    }

    public String getValue1() {
        return value1;
    }
    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }
    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            Log.e("DataSensor", e.getLocalizedMessage(), e);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return new byte[]{};
    }
}
