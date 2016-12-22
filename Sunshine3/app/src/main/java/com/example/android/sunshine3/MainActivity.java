package com.example.android.sunshine3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity, new ForecastFragment())
                    .commit();
        }
        Log.v("MainActivity: ", "Sudah dibuat.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("MainActivity: ", "Sudah mulai.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("MainActivity: ", "Sudah lanjut.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("MainActivity: ", "Sudah menahan.");
    }

    @Override
    protected void onStop() {
        super.onStop();
      
        Log.v("MainActivity: ", "Sudah berhenti.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("MainActivity: ", "Sudah mulai ulang.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("MainActivity: ", "Sudah hancur.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_map) {
            // deprecated
//            SharedPreferences sharedPrefs =
//                    PreferenceManager.getDefaultSharedPreferences(this);
//            String location = sharedPrefs.getString(
//                    getString(R.string.pref_location_key),
//                    getString(R.string.pref_location_default));
            // otomatis
            String location = Utility.getPreferredLocation(this);

            // Using the URI scheme for showing a location found on a map.  This super-handy
            // intent can is detailed in the "Common Intents" page of Android's developer site:
            // http://developer.android.com/guide/components/intents-common.html#Maps
            Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", location)
                    .build();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(geoLocation);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Log.d("MainActivity: ", "Couldn't call " + location + ", no receiving apps installed!");
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
