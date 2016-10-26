package com.gabzil.stivouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class SetPin extends Activity implements OnPinTaskCompleted, OnVerifyTaskCompleted {
    EditText pin,repin;
    Button activate,verify,forgot;
    String Pin,Repin,MobileNo;
    MyOpenHelper db;
    List<Entities> select;
    LinearLayout repinLayout;
    PinInfo info = new PinInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);

        DeclareVariables();

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("key");

        if (value==1){
            activate.setVisibility(View.GONE);
            repinLayout.setVisibility(View.GONE);
            verify.setVisibility(View.VISIBLE);
            forgot.setVisibility(View.VISIBLE);
        }

        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInfo();
                if(!ValidateData())
                    CallPinSaving();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setSecretPin(pin.getText().toString().trim());
                if(!ValidateData1())
                    VerifyPin();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelp dh = new DataHelp(getApplicationContext());
                dh.DeleteSelection();
                Intent i = new Intent(SetPin.this, LandingPage.class);
                i.putExtra("key1", 0);
                startActivity(i);
            }
        });
    }

    private void DeclareVariables() {
        pin = (EditText) findViewById(R.id.pin);
        repin = (EditText) findViewById(R.id.repin);
        activate = (Button) findViewById(R.id.activate);
        verify = (Button) findViewById(R.id.verify);
        forgot = (Button) findViewById(R.id.forgot);
        repinLayout = (LinearLayout) findViewById(R.id.RepinLayout);
        db = new MyOpenHelper(getApplicationContext());
        select = db.getSelections();
        MobileNo = select.get(0).getMobileNo();
    }

    private void setInfo() {
        Pin = pin.getText().toString().trim();
        Repin = repin.getText().toString().trim();
    }

    public void CallPinSaving() {
        info.setMobileNo(MobileNo);
        info.setSecretPin(Pin);

        SubmitPin submit = new SubmitPin(SetPin.this, this);
        submit.execute(info);
    }

    public void VerifyPin() {
        info.setMobileNo(MobileNo);
        info.setSecretPin(pin.getText().toString().trim());

        VerifyPIN submit = new VerifyPIN(SetPin.this, this);
        submit.execute(info);
    }

    private boolean ValidateData() {
        boolean error = false;
        String strmsg = "Please Enter ";
        int flag = 0;

        if (Pin.length() == 0 && Repin.length() == 0) {
            strmsg += "All the fields";
            error = true;
        } else if (Pin.length() == 0) {
            strmsg += "Pin";
            error = true;
        } else if (Pin.length() < 4) {
            strmsg += "Pin of minimun 4 digits";
            error = true;
        } else if (Repin.length() == 0) {
            strmsg += "Pin";
            flag = 1;
            error = true;
        } else if (!Pin.equals(Repin)) {
            strmsg += "And Re-Enter Pin Same";
            error = true;
        }

        if (error == true) {
            String replacedString = strmsg;
            if (flag == 1) {
                replacedString = strmsg.replace("Please Enter ", "Please Re-Enter ");
            }
            ShowAlert(replacedString);
        }
        return error;
    }

    private boolean ValidateData1() {
        boolean error = false;
        String strmsg = "Please Enter ";

        if (pin.getText().toString().trim().length() < 4) {
            strmsg += "Pin of minimun 4 digits";
            error = true;
        }

        if (error == true) {
            String replacedString = strmsg.replace("Please Enter ,", "Please Enter ");
            ShowAlert(replacedString);
        }
        return error;
    }

    public void ShowAlert(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void OnPinTaskCompleted(String results) {
        try {
            if (results != "null" && results.length() > 0) {
                if (results.equals("true")) {
                    Entities e = new Entities();
                    e.setMobileNo(MobileNo);
                    e.setOTP("No");
                    e.setPin("No");
                    e.setLogin("Yes");
                    DataHelp dh = new DataHelp(getApplicationContext());
                    if (dh.UpdateSelection(e)) {
                        Intent i = new Intent(SetPin.this, UserSelection.class);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some problem occured", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some problem occured", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error:" +e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnVerifyTaskCompleted(String results) {
        try {
            if (results != "null" && results.length() > 0) {
                if (results.equals("true")) {
                    Intent i = new Intent(SetPin.this, UserSelection.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Pin", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some problem occured", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
