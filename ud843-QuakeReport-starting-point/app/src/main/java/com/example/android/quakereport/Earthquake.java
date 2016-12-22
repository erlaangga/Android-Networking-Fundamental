package com.example.android.quakereport;

/**
 * Created by erlangga on 10/12/16.
 */

public class Earthquake {

    private Double mMagnitude;
    private String mLocation;
    private long mTime;
    private String mURL;

    public Earthquake(Double magnitude, String location, long time, String url){
        mMagnitude = magnitude;
        mLocation = location;
        mTime = time;
        mURL = url;
    }

    public String getmURL() {
        return mURL;
    }

    public Double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }


    public long getmTime() {
        return mTime;
    }

}
