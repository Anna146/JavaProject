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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


public class StationsStore extends ListActivity {
    private DBAdapter dbHelper;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private Cursor cursor;
    private EditText mLocationText;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        dbHelper = new DBAdapter(this);

        //Button backButton = (Button) findViewById(R.id.button_back);
        dbHelper.open();
        setContentView(R.layout.store);
        this.getListView().setDividerHeight(2);
        dbHelper = new DBAdapter(this);
        dbHelper.open();
        fillData();
        registerForContextMenu(getListView());
        /*backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });*/
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
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
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
        // активити вернет результат если будет вызвано с помощью этого метода
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    public void buttonInsertClick(View view) {
        createStation();
        fillData();
    }

    public void buttonBackClick(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();

    }

    String[] from, lats, longs;

    private void fillData() {
        cursor = dbHelper.fetchAll();
        startManagingCursor(cursor);

        from = new String[] { DBAdapter.KEY_NAME };
        int[] to = new int[] { R.id.label };

        // Теперь создадим адаптер массива и установим его для отображения наших данных
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
                R.layout.row, cursor, from, to);
        setListAdapter(notes);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
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

}
