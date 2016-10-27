package com.gabzil.stivouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Login extends Activity implements OnCityTaskCompleted,OnLoginTaskCompleted {
    EditText Username,Password;
    Button Login,Registration;
    TextView Reset;
    DataHelp dh;
    MyOpenHelper db;
    String Date1;
    LoginEntities MainLogin = new LoginEntities();
    String value = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Bundle b = getIntent().getExtras();
        if(b != null)
            value = b.getString("User");
        MainLogin.setUserType(value);

        DeclareVariables();
        Username.addTextChangedListener(new MyTextWatcher(Username));
        Password.addTextChangedListener(new MyTextWatcher(Password));

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoginInfo();
                if (!IsValidation()) {
                    SendLoginData();
                }
            }
        });

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
                String str = format.format(new Date());
                List<DateEntities> DateData = db.getSyncDate();
                if (DateData.size() == 0) {
                    dh.UpdateDate(str, 0);
                    GetCitiesAndStates();
                } else {
                    String Datestr = DateData.get(0).getSyncDate();
                    Date1 = Datestr;
                    if (!str.equals(Datestr)) {
                            ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected() == true) {
                                dh.UpdateDate(str, 1);
                                GetCitiesAndStates();
                            } else {
                                OpenInternetSetting();
                            }
                    } else {
                        if (MainLogin.getUserType().equals("Voucher")) {
                            Intent i = new Intent(Login.this, SignupVoucher.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(Login.this, SignupMerchant.class);
                            startActivity(i);
                        }
                    }
                }
            }
        });
    }

    private void DeclareVariables() {
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        Login = (Button) findViewById(R.id.login);
        Registration = (Button) findViewById(R.id.registration);
        Reset = (TextView) findViewById(R.id.reset);
        dh = new DataHelp(getApplicationContext());
        db = new MyOpenHelper(getApplicationContext());
    }

    public void setLoginInfo() {
        try {
            MainLogin.setUserName(Username.getText().toString().trim());
            MainLogin.setPassword(Password.getText().toString().trim());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private boolean IsValidation() {
        boolean error = false;
        String strmsg = "Please Enter ";

        if (MainLogin.getUserName().length() == 0) {
            strmsg += "Username";
            error = true;
        }
        if (MainLogin.getPassword().length() == 0) {
            strmsg += ", Password";
            error = true;
        }

        if (error == true) {
            String replacedString = strmsg.replace("Please Enter ,", "Please Enter ");
            ShowAlert(replacedString);
        }
        return error;
    }

    private void SendLoginData() {
        try {
            final SubmitLoginData p = new SubmitLoginData(Login.this, this);
            p.execute(MainLogin);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (p.getStatus() == AsyncTask.Status.RUNNING) {
                        // My AsyncTask is currently doing work in doInBackground()
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

    private void GetCitiesAndStates() {
        final CitiesAndStates p = new CitiesAndStates(Login.this, this);
        p.execute();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (p.getStatus() == AsyncTask.Status.RUNNING) {
                    // My AsyncTask is currently doing work in doInBackground()
                    p.cancel(true);
                    p.mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Network Problem, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000 * 30);
    }

    @Override
    public void OnCityTaskCompleted(String results) {
        try {
            if (!results.equals("null") & results.length() > 0) {
                JSONArray obj = new JSONArray(results);
                Gson gson = new Gson();
                try {
                    for (int i = 0; i < obj.length(); i++) {
                        String subIDInfo = obj.getJSONObject(i).toString();
                        if (subIDInfo.contains("City")) {
                            CityEntities city = gson.fromJson(subIDInfo, CityEntities.class);
                            dh.SubmitCities(city);
                        } else {
                            StateEntities state = gson.fromJson(subIDInfo, StateEntities.class);
                            dh.SubmitStates(state);
                        }
                    }
//                    List<CityEntities> cities = db.getAllCities();
//                    List<StateEntities> States = db.getAllStates();
                    if (MainLogin.getUserType().equals("Voucher")) {
                        Intent i = new Intent(Login.this, SignupVoucher.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Login.this, SignupMerchant.class);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some problem occured, Please try again", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void OpenInternetSetting() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Network settings");
        alertDialog.setMessage("Network is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
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
    public void OnLoginTaskCompleted(String results) {
        try {
            if (results != "null" && results.length() > 0) {
                if (results.equals("true")) {
                    if (MainLogin.getUserType().equals("Voucher")){
                        Toast.makeText(getApplicationContext(), "SuccessFul Login to Voucher", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "SuccessFul Login to Merchant", Toast.LENGTH_SHORT).show();
                    }
//                    Intent i = new Intent(Login.this, Login.class);
//                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Username/Password", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(Login.this,UserSelection.class);
        startActivity(i);
    }
}
