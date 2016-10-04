package com.gabzil.stivouch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginVoucher extends AppCompatActivity {
    private EditText vemail, vpass;
    private Button vlogin, vsignup;
    private String email,pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_voucher);
        vemail=(EditText)findViewById(R.id.vemail);
        vpass=(EditText)findViewById(R.id.vpass);
        vlogin=(Button)findViewById(R.id.vlogin);
        vsignup=(Button)findViewById(R.id.vsignup);

        vlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        vsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginVoucher.this, SignupVoucher.class);
                startActivity(intent);
            }
        });



    }

    private void validation(){
        email = vemail.getText().toString().trim();
        pass = vpass.getText().toString().trim();
        if(email.length()==0 ||pass.length()==0){
            if(email.length()==0){
                Toast.makeText(this, "Please enter email id", Toast.LENGTH_SHORT).show();
            }else if(pass.length()==0) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            }
        } else if(isValidEmail(email)){
            Toast.makeText(this, email, Toast.LENGTH_LONG).show();
            Toast.makeText(this, pass, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please enter valid email id", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String emailInput) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailInput);
        return matcher.matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_voucher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
