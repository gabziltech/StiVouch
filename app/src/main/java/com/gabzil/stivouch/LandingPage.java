package com.gabzil.stivouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class LandingPage extends Activity {
    CustomKeyboard mCustomKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        MyOpenHelper m = new MyOpenHelper(getApplicationContext());
        List<Entities> select = m.getSelections();
        if (select.size() == 0) {
            mCustomKeyboard = new CustomKeyboard(this, R.id.keyboardview, R.xml.hexkbd);
            mCustomKeyboard.registerEditText(R.id.mobileno);
        }
        else {
            Intent i = null;
            if (select.get(0).getOTP().equals("Yes")) {
                i = new Intent(LandingPage.this,MobileRegistration.class);
            } else if (select.get(0).getLogin().equals("Yes")) {
                i = new Intent(LandingPage.this,UserSelection.class);
            } else {
                i = new Intent(LandingPage.this,LandingPage.class);
            }
            startActivity(i);
        }
    }
}
