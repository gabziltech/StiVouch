package com.gabzil.stivouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupMerchant extends Activity implements OnMerchantTaskCompleted {
    EditText ShopName, Username, Password, ConfirmPassword, MerchantCode, MobileNo, Address, EMailID, OwnerName, OwnerMailID, OwnerMobNo, BranchCode;
    Spinner City, State;
    Button CreateAccount;
    MerchantEntities MainMerchant;
    MyOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_merchant);

        DeclareMerchantVariables();
        Username.addTextChangedListener(new MyTextWatcher(Username));
        Password.addTextChangedListener(new MyTextWatcher(Password));
        ConfirmPassword.addTextChangedListener(new MyTextWatcher(ConfirmPassword));
        EMailID.addTextChangedListener(new MyTextWatcher(EMailID));
        OwnerMailID.addTextChangedListener(new MyTextWatcher(OwnerMailID));

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVoucherInfo();
                if (!IsValidation()) {
                    SaveMerchantData();
                }
            }
        });
    }

    private void loadCityData() {
        List<String> cities = db.getAllCity();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cities);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        City.setAdapter(dataAdapter);
    }

    private void loadStateData() {
        List<String> states = db.getAllState();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, states);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        State.setAdapter(dataAdapter);
    }

    public void DeclareMerchantVariables() {
        ShopName = (EditText) findViewById(R.id.shopname);
        Username = (EditText) findViewById(R.id.m_username);
        Password = (EditText) findViewById(R.id.m_pass);
        ConfirmPassword = (EditText) findViewById(R.id.m_cnfrmpass);
        MerchantCode = (EditText) findViewById(R.id.m_code);
        MobileNo = (EditText) findViewById(R.id.m_mobileno);
        Address = (EditText) findViewById(R.id.m_address);
        EMailID = (EditText) findViewById(R.id.m_mail_id);
        OwnerName = (EditText) findViewById(R.id.m_ownername);
        OwnerMailID = (EditText) findViewById(R.id.m_owner_mail_id);
        OwnerMobNo = (EditText) findViewById(R.id.m_owner_mobileno);
        BranchCode = (EditText) findViewById(R.id.m_branch_code);
        City = (Spinner) findViewById(R.id.m_city);
        State = (Spinner) findViewById(R.id.m_state);
        CreateAccount = (Button) findViewById(R.id.m_registration);
        MainMerchant = new MerchantEntities();
        db = new MyOpenHelper(getApplicationContext());
        loadCityData();
        loadStateData();
    }

    public void setVoucherInfo() {
        try {
            MainMerchant.setShopName(ShopName.getText().toString().trim());
            MainMerchant.setUserName(Username.getText().toString().trim());
            MainMerchant.setPassword(Password.getText().toString().trim());
            MainMerchant.setMerchantCode(MerchantCode.getText().toString().trim());
            MainMerchant.setMerchantMobNo(MobileNo.getText().toString().trim());
            MainMerchant.setMerchantAddress(Address.getText().toString().trim());
            MainMerchant.setMerchantEmail(EMailID.getText().toString().trim());
            MainMerchant.setOwnerName(OwnerName.getText().toString().trim());
            MainMerchant.setOwnerEmail(OwnerMailID.getText().toString().trim());
            MainMerchant.setOwnerMobNo(OwnerMobNo.getText().toString().trim());
            MainMerchant.setBranchCode(BranchCode.getText().toString().trim());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private boolean IsValidation() {
        boolean error = false;
        String strmsg = "Please Enter ";

        if (MainMerchant.getShopName().length() == 0) {
            strmsg += "Shop Name";
            error = true;
        }
        if (MainMerchant.getUserName().length() == 0) {
            strmsg += ", Username";
            error = true;
        } else if (MainMerchant.getUserName().length() < 6) {
            strmsg += ", Username of minimum 6 characters";
            error = true;
        }
        if (MainMerchant.getPassword().length() == 0) {
            strmsg += ", Password";
            error = true;
        } else if (MainMerchant.getPassword().length() < 6) {
            strmsg += ", Password of minimum 6 characters";
            error = true;
        } else if (ConfirmPassword.getText().toString().trim().length() == 0) {
            strmsg += ", Confirm Password";
            error = true;
        } else if (!MainMerchant.getPassword().equals(ConfirmPassword.getText().toString().trim())) {
            strmsg += ", Password & Confirm Password Same";
            error = true;
        }
        if (MainMerchant.getMerchantCode().length() == 0) {
            strmsg += ", Merchant Code";
            error = true;
        }
        if (MainMerchant.getMerchantMobNo().length() == 0) {
            strmsg += ", Mobile No";
            error = true;
        } else if (MainMerchant.getMerchantMobNo().length() < 10) {
            strmsg += ", 10 digit Mobile No";
            error = true;
        } else if (MainMerchant.getMerchantMobNo().equals("0000000000")) {
            strmsg += ", Valid Mobile No";
            error = true;
        }
        if (MainMerchant.getMerchantAddress().length() == 0) {
            strmsg += ", Address";
            error = true;
        }
        if (City.getSelectedItem().toString().equals("Select")) {
            strmsg += ", City";
            error = true;
        }
        if (State.getSelectedItem().toString().equals("Select")) {
            strmsg += ", State";
            error = true;
        }
        if (MainMerchant.getMerchantEmail().length() == 0) {
            strmsg += ", Merchant Email ID";
            error = true;
        } else if (!isValidEmail(MainMerchant.getMerchantEmail())) {
            strmsg += ", Valid Merchant Email ID";
            error = true;
        }
        if (MainMerchant.getOwnerName().length() == 0) {
            strmsg += ", Owner Name";
            error = true;
        }
        if (MainMerchant.getOwnerEmail().length() == 0) {
            strmsg += ", Owner Email ID";
            error = true;
        } else if (!isValidEmail(MainMerchant.getOwnerEmail())) {
            strmsg += ", Valid Owner Email ID";
            error = true;
        }
        if (MainMerchant.getOwnerMobNo().length() == 0) {
            strmsg += ", Owner Mobile No";
            error = true;
        } else if (MainMerchant.getOwnerMobNo().length() < 10) {
            strmsg += ", 10 digit Owner Mobile No";
            error = true;
        } else if (MainMerchant.getOwnerMobNo().equals("0000000000")) {
            strmsg += ", Valid Mobile No";
            error = true;
        }
        if (MainMerchant.getBranchCode().length() == 0) {
            strmsg += ", Branch Code";
            error = true;
        }

        if (error == true) {
            String replacedString = strmsg.replace("Please Enter ,", "Please Enter ");
            ShowAlert(replacedString);
        }
        return error;
    }

    private boolean isValidEmail(String emailInput) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailInput);
        return matcher.matches();
    }

    private void SaveMerchantData() {
        try {
            CityEntities city = db.getCityByName(City.getSelectedItem().toString());
            MainMerchant.setCityID(city.CityID);
            StateEntities state = db.getStateByName(State.getSelectedItem().toString());
            MainMerchant.setStatesID(state.StatesID);
            final SubmitMerchant p = new SubmitMerchant(SignupMerchant.this, this);
            p.execute(MainMerchant);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (p.getStatus() == AsyncTask.Status.RUNNING) {
                        p.cancel(true);
                        p.mProgress.dismiss();
                        Toast.makeText(getApplicationContext(), "Network Problem, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 1000 * 30);
        } catch (Exception e) {
            e.getMessage();
        }
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
    public void OnMerchantTaskCompleted(String results) {
        try {
            if (results != "null" && results.length() > 0) {
                if (results.equals("true")) {
                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignupMerchant.this, Login.class);
                    i.putExtra("User", "Merchant");
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Some problem occured", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some problem occured", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SignupMerchant.this,Login.class);
        i.putExtra("User", "Merchant");
        startActivity(i);
    }
}
