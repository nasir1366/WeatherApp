package com.learn.weatherapplication.data;

import android.content.ContentValues;
import android.database.Cursor;

public class CityModel {
    private Long id;
    private String name="";
    private double lat;
    private double lon;
    private String countryCode="";
    private boolean selected = false;


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return name.toUpperCase() + ", " + countryCode;
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        cv.put("name",name);
        cv.put("lat",lat);
        cv.put("lon",lon);
        cv.put("countryCode",countryCode);
        cv.put("selected", selected ? 1 : 0);
        return cv;
    }

    public static  ContentValues getContentValues(Long id,String name,Double lat,Double lon,String countryCode,boolean selected){
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        cv.put("name",name);
        cv.put("lat",lat);
        cv.put("lon",lon);
        cv.put("countryCode",countryCode);
        cv.put("selected", selected ? 1 : 0);
        return cv;

    }

    public static CityModel cursorToCityModel(Cursor cursor){
        CityModel city = new CityModel();
        city.setId(cursor.getLong(cursor.getColumnIndex("id")));
        city.setName(cursor.getString(cursor.getColumnIndex("name")));
        city.setLat(cursor.getDouble(cursor.getColumnIndex("lat")));
        city.setLon(cursor.getDouble(cursor.getColumnIndex("lon")));
        city.setCountryCode(cursor.getString(cursor.getColumnIndex("countryCode")));
        return city;
    }
}
