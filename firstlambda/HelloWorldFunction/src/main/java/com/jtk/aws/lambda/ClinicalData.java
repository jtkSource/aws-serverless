package com.jtk.aws.lambda;

public class ClinicalData {
    private String heartRate;
    private String bp;

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }
}
