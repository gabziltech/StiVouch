package com.gabzil.stivouch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupVoucher extends AppCompatActivity {
    private EditText vusername,vemail,vdob,vmobileno,vpassword,vcompname,vcompid;
    private Button vsubmit;
    private TextView userdob;
    private CalendarView calendarView;
    private Spinner vstate,vcountry;
    int sindex,cindex;
    private String username,email,dob,mobileno,state,country,password,compname,compid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_voucher);

        vusername=(EditText)findViewById(R.id.vusername);
        vemail=(EditText)findViewById(R.id.vuseremail);
        calendarView = (CalendarView) findViewById(R.id.vcal);
        userdob=(TextView)findViewById(R.id.vdob);
        vmobileno=(EditText)findViewById(R.id.vmobileno);
        vstate=(Spinner)findViewById(R.id.vstate);
        vcountry=(Spinner)findViewById(R.id.vcounty);
        vpassword=(EditText)findViewById(R.id.vpassword);
        vcompname=(EditText)findViewById(R.id.vcompanyname);
        vcompid=(EditText)findViewById(R.id.vcompanyid);
        vsubmit=(Button)findViewById(R.id.vsubmit);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub
                String date=dayOfMonth+" - "+month+" - "+year;
               // dob=userdob.setText(date);
                Toast.makeText(getBaseContext(),"Selected Date is\n\n"
                                +dayOfMonth+" : "+month+" : "+year ,
                        Toast.LENGTH_LONG).show();
            }
        });
        vsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }


    private void validation(){
        username=vusername.getText().toString().trim();
        email=vemail.getText().toString().trim();
       // dob=vdob.getText().toString().trim();
        mobileno=vmobileno.getText().toString().trim();
        sindex=vstate.getSelectedItemPosition();
        cindex=vcountry.getSelectedItemPosition();
        state=vstate.getSelectedItem().toString().trim();
        country=vcountry.getSelectedItem().toString().trim();
        password=vpassword.getText().toString().trim();
        compname=vcompname.getText().toString().trim();
        compid=vcompid.getText().toString().trim();

        if(username.length()==0){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }else if(email.length()==0){
            Toast.makeText(this, "Please enter email-id", Toast.LENGTH_SHORT).show();
        }else if(mobileno.length()==0){
            Toast.makeText(this, "Please enter mobileno", Toast.LENGTH_SHORT).show();
        }else if(sindex==0){
            Toast.makeText(this, "Please select your state", Toast.LENGTH_SHORT).show();
        }else if(cindex==0){
            Toast.makeText(this, "Please select your country", Toast.LENGTH_SHORT).show();
        }else if(password.length()==0){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }else if(isValidEmail(email)){
            Intent intent=new Intent(SignupVoucher.this,SignupOTP.class);
               startActivity(intent);
        }else{
            Toast.makeText(this, "Please enter valid email-id", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_signup_voucher, menu);
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
