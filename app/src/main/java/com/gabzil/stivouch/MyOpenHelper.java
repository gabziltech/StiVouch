package com.gabzil.stivouch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StiVouch";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "PageSelection";
    public static final String TABLE_NAME1 = "City";
    public static final String TABLE_NAME2 = "State";

    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE PageSelection (Id Integer PRIMARY KEY AUTOINCREMENT,MobileNo TEXT,"
                + "OTP TEXT, Pin TEXT, Login TEXT)");

        db.execSQL("CREATE TABLE City (Id Integer PRIMARY KEY AUTOINCREMENT,CityID Integer,"
                + "CityName TEXT)");

        db.execSQL("CREATE TABLE State (Id Integer PRIMARY KEY AUTOINCREMENT,StateID Integer,"
                + "StateName TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME2);
        onCreate(db);
        System.out.println("On Upgrade Call");
    }

    public List<Entities> getSelections() {
        List<Entities> SelectionList = new ArrayList<Entities>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Entities e = new Entities();
                    e.setID(Integer.parseInt(cursor.getString(0)));
                    e.setMobileNo(cursor.getString(1));
                    e.setOTP(cursor.getString(2));
                    e.setPin(cursor.getString(3));
                    e.setLogin(cursor.getString(4));
                    // Adding contact to list
                    SelectionList.add(e);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            e.getMessage();
        }

        // return contact list
        return SelectionList;
    }

    public CityEntities getCityByID(int cityID) {
        CityEntities city = null;
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NAME1 + " where CityID= "+ cityID;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    city = new CityEntities();
                    city.setCityID(Integer.parseInt(cursor.getString(1)));
                    city.setCity(cursor.getString(2));
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.getMessage();
        }

        return city;
    }

    public CityEntities getCityByName(String cityName) {
        CityEntities city = null;
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NAME1 + " where CityName = '" +cityName+ "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    city = new CityEntities();
                    city.setCityID(Integer.parseInt(cursor.getString(1)));
                    city.setCity(cursor.getString(2));
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.getMessage();
        }

        return city;
    }

    public List<CityEntities> getAllCities() {
        List<CityEntities> CityList = new ArrayList<CityEntities>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NAME1;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    CityEntities e = new CityEntities();
                    e.setCityID(Integer.parseInt(cursor.getString(1)));
                    e.setCity(cursor.getString(2));

                    CityList.add(e);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            e.getMessage();
        }

        return CityList;
    }

    public List<String> getAllCity() {
        List<String> CityList = new ArrayList<String>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NAME1;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    CityList.add(cursor.getString(2));
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            e.getMessage();
        }

        return CityList;
    }

    public StateEntities getStateByID(int stateID) {
        StateEntities state = null;
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NAME2 + " where StateID= "+ stateID;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    state = new StateEntities();
                    state.setStateID(Integer.parseInt(cursor.getString(1)));
                    state.setState(cursor.getString(2));
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.getMessage();
        }

        return state;
    }

    public StateEntities getStateByName(String stateName) {
        StateEntities state = null;
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NAME2 + " where StateName= "+ stateName;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    state = new StateEntities();
                    state.setStateID(Integer.parseInt(cursor.getString(1)));
                    state.setState(cursor.getString(2));
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.getMessage();
        }

        return state;
    }

    public List<StateEntities> getAllStates() {
        List<StateEntities> StateList = new ArrayList<StateEntities>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NAME2;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    StateEntities e = new StateEntities();
                    e.setStateID(Integer.parseInt(cursor.getString(1)));
                    e.setState(cursor.getString(2));

                    StateList.add(e);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            e.getMessage();
        }

        return StateList;
    }
}
