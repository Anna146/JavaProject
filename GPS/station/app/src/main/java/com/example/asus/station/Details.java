package com.example.asus.station;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.asus.station.DBAdapter;

public class Details extends Activity {
    private EditText mTitleText;
    private EditText mLatText;
    private EditText mLonText;
    private Long mRowId;
    private DBAdapter mDbHelper;
    private Spinner mCategory;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mDbHelper = new DBAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.edit);
        //mCategory = (Spinner) findViewById(R.id.category);
        mTitleText = (EditText) findViewById(R.id.edit_name);
        mLatText = (EditText) findViewById(R.id.edit_lat);
        mLonText = (EditText) findViewById(R.id.edit_lon);

        Button confirmButton = (Button) findViewById(R.id.edit_button);
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        mRowId = (bundle == null) ? null : (Long) bundle
                .getSerializable(DBAdapter.KEY_ROWID);
        if (extras != null) {
            mRowId = extras.getLong(DBAdapter.KEY_ROWID);
        }
        populateFields();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor todo = mDbHelper.fetch(mRowId);
            startManagingCursor(todo);
            /*String category = todo.getString(todo
                    .getColumnIndexOrThrow(DBAdapter.KEY_CATEGORY));

            for (int i=0; i<mCategory.getCount();i++){

                String s = (String) mCategory.getItemAtPosition(i);
                Log.e(null, s +" " + category);
                if (s.equalsIgnoreCase(category)){
                    mCategory.setSelection(i);
                }
            }*/

            mTitleText.setText(todo.getString(2));
            mLatText.setText(todo.getString(todo
                    .getColumnIndexOrThrow(DBAdapter.KEY_LATITUDE)));
            mLonText.setText(todo.getString(todo
                    .getColumnIndexOrThrow(DBAdapter.KEY_LONGITUDE)));
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DBAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        //String category = (String) mCategory.getSelectedItem();
        String name = mTitleText.getText().toString();
        int lat = Integer.parseInt(mLatText.getText().toString());
        int lon = Integer.parseInt(mLonText.getText().toString());

        if (mRowId == null) {
            long id = mDbHelper.create(name, lat, lon);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.update(mRowId, name, lat, lon);
        }
    }
}