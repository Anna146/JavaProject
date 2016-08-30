package com.example.asus.station;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

public class Overview extends ListActivity {
    private DBAdapter dbHelper;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private Cursor cursor;
    private EditText mLocationText;
    private Button buttonGetLocation;
    private Button buttonRadius;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stations);
        this.getListView().setDividerHeight(2);
        dbHelper = new DBAdapter(this);
        dbHelper.open();
        if (mobileLocation != null) {
            queryStations();
        }
        fillData();
        registerForContextMenu(getListView());
        mLocationText = (EditText) findViewById(R.id.editTextShowLocation);
        super.onCreate(savedInstanceState);

        buttonGetLocation = (Button) findViewById(R.id.buttonGetLocation);

        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonGetLocationClick();
            }
        });

        buttonRadius = (Button) findViewById(R.id.buttonRadius);

        buttonRadius.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonRadiusClick();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    // Реакция на выбор меню
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createStation();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createStation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();
                dbHelper.delete(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createStation() {
        Intent i = new Intent(this, Details.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, Details.class);
        i.putExtra(DBAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }


    public void buttonShowClick(View view) {
        Intent i = new Intent(this, StationsStore.class);
        startActivity(i);
    }

    public void buttonRadiusClick() {
        double delt = 10;
        Intent i = new Intent(this, Radius.class);
        i.putExtra("DELTA", delt);
        startActivityForResult(i, 2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == 2) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                deltaCoord = extras.getDouble("DELTA");
            }
        }
        fillData();

    }

    String[] from, lats, longs;

    private void fillData() {
        cursor = dbHelper.fetchAll();
        startManagingCursor(cursor);

        from = new String[] { DBAdapter.KEY_NAME };
        lats = new String[] {DBAdapter.KEY_LATITUDE};
        longs = new String[] {DBAdapter.KEY_LONGITUDE};
        int[] to = new int[] { R.id.label };

        // Теперь создадим адаптер массива и установим его для отображения наших данных
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
                R.layout.row, cursor, from, to);
        setListAdapter(notes);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    private LocationManager locManager;
    private LocationListener locListener;
    private Location mobileLocation;


    private void getCurrentLocation() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

    public void buttonGetLocationClick() {
        getCurrentLocation(); // gets the current location and update mobileLocation variable

        if (mobileLocation != null) {
            locManager.removeUpdates(locListener); // This needs to stop getting the location data and save the battery power.

            String londitude = "Londitude: " + mobileLocation.getLongitude();
            String latitude = "Latitude: " + mobileLocation.getLatitude();

            mLocationText.setText(londitude + "\n" + latitude);

            queryStations();
        } else {
            mLocationText.setText("Sorry, location is not determined");
        }
    }

    double deltaCoord = 10;


    public void queryStations() {
        int[] to = new int[] { R.id.label };

        cursor = dbHelper.fetchCoord(mobileLocation.getLatitude(), mobileLocation.getLongitude(), deltaCoord);
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
                R.layout.row, cursor, from, to);
        setListAdapter(notes);
    }


    public void queryStations1()
    {
        cursor = dbHelper.fetchAll();

        ArrayList<String> curr = new ArrayList<String>();
        int j=0;
        String name = new String();
        String lg = new String();
        String lt = new String();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            lg = cursor.getString(cursor
                    .getColumnIndex(DBAdapter.KEY_LONGITUDE));
            lt = cursor.getString(cursor
                    .getColumnIndex(DBAdapter.KEY_LATITUDE));
            name = cursor.getString(cursor
                    .getColumnIndex(DBAdapter.KEY_NAME));
            if (compare(lt, mobileLocation.getLatitude(), lg, mobileLocation.getLongitude()))
            {
                curr.add(name);
            }
            j++;
        }


        String[] currArr = curr.toArray(new String[curr.size()]);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	R.layout.list_item, currArr);

        getListView().setAdapter(adapter);


    }

    public boolean compare(String lat1, double lat2, String long1, double long2){
        double leftLat1 = Double.parseDouble(lat1) - deltaCoord;
        double rightLat1 = Double.parseDouble(lat1) + deltaCoord;
        double leftLong1 = Double.parseDouble(long1) - deltaCoord;
        double rightLong1 = Double.parseDouble(long1) + deltaCoord;
        if (lat2 > leftLat1 && lat2 < rightLat1 && long2 > leftLong1 && long2 < rightLong1) {
            return true;
        }
        return false;
    }
}