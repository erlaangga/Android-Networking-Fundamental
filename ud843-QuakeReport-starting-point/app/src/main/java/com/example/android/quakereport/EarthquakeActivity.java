/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<EarthquakeAdapter>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
//    private String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&limit=10";
    private String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        // Create a fake list of earthquake locations.
//        ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdapter adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        updateUI(adapter);
//        EarthquakeTask earthquakeTask = new EarthquakeTask();
//        earthquakeTask.execute(USGS_REQUEST_URL);

            getLoaderManager().initLoader(0,null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI(final EarthquakeAdapter adapter){
        ProgressBar emptyprogressBar;

        if (adapter == null) {
            TextView noData = (TextView) findViewById(R.id.no_data_view);
            emptyprogressBar = (ProgressBar) findViewById(R.id.progressBar);
            emptyprogressBar.setVisibility(TextView.GONE);
            noData.setVisibility(TextView.VISIBLE);
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetworkInfo == null || !activeNetworkInfo.isConnected())noData.setText("No connection");
            return;
        }
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        emptyprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        earthquakeListView.setEmptyView(emptyprogressBar);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getmURL());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
        ;
        earthquakeListView.setAdapter(adapter);
    }

    @Override
    public Loader<EarthquakeAdapter> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMag = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMag);
        uriBuilder.appendQueryParameter("orderby","time");
        uriBuilder.appendQueryParameter("eventtype","earthquake");

        System.out.println("Isi uriBuilder:" + uriBuilder);
        System.out.println("String uriBuilder: "+uriBuilder.toString());

//        return new EarthquakeLoader(EarthquakeActivity.this, USGS_REQUEST_URL);
        return new EarthquakeLoader(EarthquakeActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<EarthquakeAdapter> loader, EarthquakeAdapter data) {
//        if (data==null)return;
        updateUI(data);
    }

    @Override
    public void onLoaderReset(Loader<EarthquakeAdapter> loader) {

    }

// Diganti menggunakan Loader :D
//    private class EarthquakeTask extends AsyncTask<String, Void, EarthquakeAdapter>{
//        @Override
//        protected EarthquakeAdapter doInBackground(String... params) {
//            System.out.println("Kerjanin di belakang");
//            URL url = createURL(params[0]);
//            String jsonEarthquakes=null;
//            try {
//                jsonEarthquakes = makeHttpRequest(url);
////                if (jsonEarthquakes.length() <1 || jsonEarthquakes.equals(""))return null;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes(jsonEarthquakes);
//            EarthquakeAdapter adapter = new EarthquakeAdapter(getApplicationContext(), earthquakes);
//            return adapter;
//        }
//
//        private URL createURL(String urlString){
//            URL url = null;
//            try {
//                url = new URL(urlString);
//                return url;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        private String makeHttpRequest(URL url) throws IOException {
//            if (url == null) return null;
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setConnectTimeout(1000);
//            urlConnection.setReadTimeout(1000);
//            urlConnection.connect();
//            String jsonResponse = "";
//            if (urlConnection.getResponseCode() == 200) {
//                InputStream inputStream = urlConnection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                StringBuilder stringBuilder = new StringBuilder();
//                String string = bufferedReader.readLine();
//                while (string != null) {
//                    stringBuilder.append(string);
//                    string = bufferedReader.readLine();
//                }
//                urlConnection.disconnect();
//                inputStream.close();
//                jsonResponse = stringBuilder.toString();
//            }
//            return jsonResponse;
//        }
//
//        @Override
//        protected void onPostExecute(EarthquakeAdapter adapter) {
//            super.onPostExecute(adapter);
//            if(adapter == null){
//                return;
//            }
//            updateUI(adapter);
//        }
//    }
}
