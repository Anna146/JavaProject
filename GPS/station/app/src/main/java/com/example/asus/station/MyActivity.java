package com.example.asus.station;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MyActivity extends ActionBarActivity {

    private EditText editTextShowLocation;
    private Button buttonGetLocation;

    private LocationManager locManager;
    private LocationListener locListener;
    private Location mobileLocation;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        editTextShowLocation = (EditText) findViewById(R.id.editTextShowLocation);

        buttonGetLocation = (Button) findViewById(R.id.buttonGetLocation);
        buttonGetLocation.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                buttonGetLocationClick();
            }
        });
    }

    /** Gets the current location and update the mobileLocation variable*/
    private void getCurrentLocation() {
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                mobileLocation = location;
            }
        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
    }

    private void buttonGetLocationClick() {
        getCurrentLocation(); // gets the current location and update mobileLocation variable

        if (mobileLocation != null) {
            locManager.removeUpdates(locListener); // This needs to stop getting the location data and save the battery power.

            String londitude = "Londitude: " + mobileLocation.getLongitude();
            String latitude = "Latitude: " + mobileLocation.getLatitude();
            String altitiude = "Altitiude: " + mobileLocation.getAltitude();
            String accuracy = "Accuracy: " + mobileLocation.getAccuracy();
            String time = "Time: " + mobileLocation.getTime();

            editTextShowLocation.setText(londitude + "\n" + latitude + "\n"
                    + altitiude + "\n" + accuracy + "\n" + time);
        } else {
            editTextShowLocation.setText("Sorry, location is not determined");
        }
    }
}
