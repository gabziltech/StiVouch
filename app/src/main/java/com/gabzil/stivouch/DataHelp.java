package com.gabzil.stivouch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DataHelp {
    SQLiteDatabase db;
    Context context;
    private MyOpenHelper db1;

    public DataHelp(Context con) {
        this.context = con;
        SQLiteOpenHelper myHelper = new MyOpenHelper(this.context);
        this.db = myHelper.getWritableDatabase();
        this.db1 = new MyOpenHelper(this.context);
    }

    public Boolean UpdateSelection(Entities e) {
        try {
            ContentValues conV = new ContentValues();
            conV.put("MobileNo", e.getMobileNo());
            conV.put("OTP", e.getOTP());
            conV.put("Pin", e.getPin());
            conV.put("Login", e.getLogin());
            List<Entities> Selection = db1.getSelections();
            if (Selection.size() == 0) {
                db.insert(MyOpenHelper.TABLE_NAME, null, conV);
            } else {
                String where = " ID = " + Selection.get(0).getID();
                db.update(MyOpenHelper.TABLE_NAME, conV, where, null);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void DeleteSelection() {
        db.delete(MyOpenHelper.TABLE_NAME, null, null);
    }

    public Boolean SubmitCities(CityEntities e) {
        try {
            ContentValues conV = new ContentValues();
            conV.put("CityID", e.getCityID());
            conV.put("CityName", e.getCity());
            CityEntities city = db1.getCityByID(e.getCityID());
            if (city == null) {
                db.insert(MyOpenHelper.TABLE_NAME1, null, conV);
            } else {
                String where = "CityID=" + e.getCityID();
                db.update(MyOpenHelper.TABLE_NAME1, conV, where, null);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Boolean SubmitStates(StateEntities e) {
        try {
            ContentValues conV = new ContentValues();
            conV.put("StateID", e.getStateID());
            conV.put("StateName", e.getState());
            StateEntities city = db1.getStateByID(e.getStateID());
            if (city == null) {
                db.insert(MyOpenHelper.TABLE_NAME2, null, conV);
            } else {
                String where = "StateID=" + e.getStateID();
                db.update(MyOpenHelper.TABLE_NAME2, conV, where, null);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void dbClose(SQLiteDatabase db) {
        db.close();
    }
}
