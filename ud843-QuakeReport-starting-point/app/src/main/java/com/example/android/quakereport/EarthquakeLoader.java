package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by erlangga on 26/12/16.
 */

public class EarthquakeLoader extends AsyncTaskLoader<EarthquakeAdapter> {

    String url;
    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public EarthquakeAdapter loadInBackground() {
        System.out.println("Muat di belakang");
        if(this.url==null || this.url.length()<1)return null;
        URL url = QueryUtils.createURL(this.url);
        String jsonEarthquakes=null;
        try {
            jsonEarthquakes = QueryUtils.makeHttpRequest(url);
//                if (jsonEarthquakes.length() <1 || jsonEarthquakes.equals(""))return null;
        } catch (IOException e) {
            return null;
        }
        ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes(jsonEarthquakes);
        EarthquakeAdapter adapter = new EarthquakeAdapter(getContext(), earthquakes);
        return adapter;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
