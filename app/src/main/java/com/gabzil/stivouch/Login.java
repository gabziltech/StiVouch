package com.gabzil.stivouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.List;

public class Login extends Activity implements OnCityTaskCompleted {
    EditText Username,Password;
    Button Login,Registration;
    TextView Reset;
    DataHelp dh;
    MyOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        DeclareVariables();

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCitiesAndStates();
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

    private void GetCitiesAndStates() {
        CitiesAndStates p = new CitiesAndStates(Login.this, this);
        p.execute();
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                if (p.getStatus() == AsyncTask.Status.RUNNING) {
//                    // My AsyncTask is currently doing work in doInBackground()
//                    p.cancel(true);
//                    p.mProgress.dismiss();
//                    Toast.makeText(getActivity(), "Network Problem, Please try again", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, 1000 * 30);
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
                    List<CityEntities> cities = db.getAllCities();
                    List<StateEntities> States = db.getAllStates();

                    Intent i = new Intent(Login.this, SignupVoucher.class);
                    startActivity(i);
                } catch (Exception e) {
                    e.getMessage();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some problem occured, Sync is not done", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
