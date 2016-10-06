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

    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE PageSelection (Id Integer PRIMARY KEY AUTOINCREMENT,MobileNo TEXT,"
                + "OTP TEXT, Login TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
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
                    e.setLogin(cursor.getString(3));
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
}
