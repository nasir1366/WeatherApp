package com.learn.weatherapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.learn.weatherapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CityDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DBNAME = "db_city";
    public static final String TABLE_CITY = "tb_city";
    private static final String CMD_CREATE_CITY_TABLE = "CREATE TABLE IF NOT EXISTS '"+TABLE_CITY+"'("+
            "'id' INTEGER PRIMARY KEY NOT NULL, "+
            "'name' TEXT, "+
            "'lat' DOUBLE, "+
            "'lon' DOUBLE, "+
            "'countryCode' TEXT, "+
            "'selected' INTEGER"+
            ")";
    public static final String[] SELECT_ALL_COLUMNS = {"id","name","lat","lon","countryCode"};
    private static final String TAG_DB = "dbHelper";
    private final Context context;



    public CityDbHelper(@Nullable Context context) {
        super(context, DBNAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CMD_CREATE_CITY_TABLE);
        Log.d(TAG_DB,"Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CITY);
        onCreate(db);
    }

    public void initContents(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                assert context != null;
                InputStream stream = context.getResources().openRawResource(R.raw.city_list);
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                SQLiteDatabase db = getWritableDatabase();
                try {
                    while ((line = reader.readLine())!= null) {
                        //split each line to 5 strings with "\t"
                        String[] data = line.split("\t");
                        //if length of data is < 5 ignore this and read next line
                        if(data.length<5)
                            continue;
                        //insert one city to database
                        db.insert(TABLE_CITY,null,
                                CityModel.getContentValues(
                                        Long.valueOf(data[0]),
                                        data[1],
                                        Double.parseDouble(data[2]),
                                        Double.parseDouble(data[3]),data[4],
                                        false)
                        );

                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                db.close();

            }
        });

        thread.start();

    }

    public void insertCityToDb(CityModel city){
        SQLiteDatabase db = getWritableDatabase();
        long insertId = db.insert(TABLE_CITY,null,city.getContentValues());
        Log.i(TAG_DB,"city inserted with id:"+insertId);
        db.close();
    }

    public List<CityModel> getCities(String selection, String[] selectionArgs, String limit){
        List<CityModel> cityList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(true,TABLE_CITY,SELECT_ALL_COLUMNS,selection,selectionArgs,null,null,"countryCode,name",limit);
//        Log.d(TAG_DB,"curser returned: "+cursor.getCount()+" records.");
        if(cursor.moveToFirst()){
            do{
                cityList.add(CityModel.cursorToCityModel(cursor));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cityList;
    }



    public void updateCitySelected(long id, boolean selected){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("selected",selected ? 1 : 0);
        db.update(TABLE_CITY,cv,"id="+id,null);
        db.close();
    }
}
