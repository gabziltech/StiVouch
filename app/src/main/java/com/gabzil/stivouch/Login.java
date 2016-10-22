package com.gabzil.stivouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {
    EditText Username,Password;
    Button Login,Registration;
    TextView Reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        DeclareVariables();

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,SignupVoucher.class);
                startActivity(i);
            }
        });
    }

    private void DeclareVariables() {
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        Login = (Button) findViewById(R.id.login);
        Registration = (Button) findViewById(R.id.registration);
        Reset = (TextView) findViewById(R.id.reset);
    }
}
