package com.example.asus.station;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {

    // поля базы данных
    public static final String KEY_ROWID = "_id";
    public static final String KEY_CATEGORY = "code";
    public static final String KEY_NAME = "name";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";
    private static final String DATABASE_TABLE = "stations";
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public static int LAST_REC = 1;

    public DBAdapter(Context context) {
        this.context = context;
    }

    public DBAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * создать новый элемент списка. если создан успешно - возвращается номер строки rowId
     * иначе -1
     */
    public long create(String name, int lat, int longt) {
        ContentValues initialValues = createContentValues(name, lat, longt);

        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * обновить список
     */
    public boolean update(long rowId, String name, int lat, int longt) {
        ContentValues updateValues = createContentValues(name, lat, longt);

        return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
                + rowId, null) > 0;
    }

    /**
     * удаляет элемент списка
     */
    public boolean delete(long rowId) {
        return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * возвращает курсор со всеми элементами списка
     *
     * @return курсор с результатами всех записей
     */
    public Cursor fetchAll() {
        return database.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_CATEGORY,
                        KEY_NAME, KEY_LATITUDE, KEY_LONGITUDE }, null, null, null,
                null, null);
    }

    /**
     * возвращает курсор, спозиционированный на указанной записи
     */
    public Cursor fetch(long rowId) throws SQLException {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROWID, KEY_CATEGORY, KEY_NAME, KEY_LATITUDE, KEY_LONGITUDE },
                KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchCoord(double lat, double longt, double dist) throws SQLException {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROWID, KEY_CATEGORY, KEY_NAME, KEY_LATITUDE, KEY_LONGITUDE },
                KEY_LATITUDE + "<" + (int)(lat + dist) + " AND " + KEY_LONGITUDE + ">" + (int)(longt - dist) + " AND " + KEY_LONGITUDE + "<" + (int)(longt + dist) + " AND " + KEY_LATITUDE + ">" + (int)(lat - dist), null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    private ContentValues createContentValues(String name,int lat,
                                              int lon) {
        ContentValues values = new ContentValues();
        LAST_REC += 1;
        values.put(KEY_CATEGORY, String.valueOf(LAST_REC));
        values.put(KEY_NAME, name);
        values.put(KEY_LATITUDE, String.valueOf(lat));
        values.put(KEY_LONGITUDE, String.valueOf(lon));
        return values;
    }

    public int get_count() {
        return LAST_REC;
    }
}