package com.gabzil.stivouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupVoucher extends Activity implements OnVoucherTaskCompleted {
    private EditText vouchername,vouchermail,vouchermobileno,voucherpassword,vouchercompany,vouchercompanyID;
    private TextView voucherdob;
    private Button submit;
    private Spinner voucherstate,vouchercountry;
    Calendar myCalendar;
    VoucherEntities MainVoucher = new VoucherEntities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_voucher);

        DeclareCustomerVariables();

        SimpleDateFormat myFormat = new SimpleDateFormat("MMM dd, yyyy");
        String currentDateTimeString = myFormat.format(new Date());
        voucherdob.setText(currentDateTimeString.substring(0, 12));
        voucherdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetDate();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCustInformation();
                if (!IsValidation()) {
                    SaveCustomerData();
                }
            }
        });
    }

    public void DeclareCustomerVariables(){
        vouchername=(EditText)findViewById(R.id.vusername);
        vouchermail=(EditText)findViewById(R.id.vuseremail);
        voucherdob=(TextView)findViewById(R.id.vdob);
        vouchermobileno=(EditText)findViewById(R.id.vmobileno);
        voucherstate=(Spinner)findViewById(R.id.vstate);
        vouchercountry=(Spinner)findViewById(R.id.vcounty);
        voucherpassword=(EditText)findViewById(R.id.vpassword);
        vouchercompany=(EditText)findViewById(R.id.vcompanyname);
        vouchercompanyID=(EditText)findViewById(R.id.vcompanyid);
        submit=(Button)findViewById(R.id.vsubmit);
        myCalendar = Calendar.getInstance();
    }

    private void SaveCustomerData() {
        final SubmitVoucher p = new SubmitVoucher(getApplicationContext(), this);
        p.execute(MainVoucher);
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                if (p.getStatus() == AsyncTask.Status.RUNNING) {
//                    // My AsyncTask is currently doing work in doInBackground()
//                    p.cancel(true);
//                    p.mProgress.dismiss();
//                    Toast.makeText(getApplicationContext(), "Network Problem, Please try again", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, 1000 * 30);
    }

    public void SetDate() {
        try {
            new DatePickerDialog(getApplicationContext(), date1, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
        catch (Exception e) {
            e.getMessage();
        }
    }

    DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        voucherdob.setText(df.format(myCalendar.getTime()));
    }

    public void setCustInformation(){
        MainVoucher.setVoucherName(vouchername.getText().toString().trim());
        MainVoucher.setVoucherMail(vouchermail.getText().toString().trim());
        MainVoucher.setDOB(voucherdob.getText().toString().trim());
        MainVoucher.setMobileNo(vouchermobileno.getText().toString().trim());
        MainVoucher.setState(voucherstate.getSelectedItem().toString().trim());
        MainVoucher.setCountry(vouchercountry.getSelectedItem().toString().trim());
        MainVoucher.setPassword(voucherpassword.getText().toString().trim());
        MainVoucher.setCompanyName(vouchercompany.getText().toString().trim());
        MainVoucher.setCompanyID(Integer.parseInt(vouchercompanyID.getText().toString().trim()));
    }

    private boolean IsValidation() {
        boolean error = false;
        String strmsg = "Please Enter ";

        if (MainVoucher.getVoucherName().trim().length() == 0) {
            strmsg += "Voucher Name";
            error = true;
        }
        if (MainVoucher.getVoucherMail().trim().length() == 0) {
            strmsg += ", Mail ID";
            error = true;
        } else if(!isValidEmail(MainVoucher.getVoucherMail().trim())) {
            strmsg += ", Valid Mail ID";
            error = true;
        }
        if (MainVoucher.getDOB().trim().length() == 0) {
            strmsg += ", DOB";
            error = true;
        }
        if (MainVoucher.getMobileNo().trim().length() == 0) {
            strmsg += ", Mobile No";
            error = true;
        }
        if (voucherstate.getSelectedItem().toString().equals("Select State")) {
            strmsg += ", State";
            error = true;
        }
        if (vouchercountry.getSelectedItem().toString().equals("Select Country")) {
            strmsg += ", Country";
            error = true;
        }
        if (MainVoucher.getPassword().trim().length() == 0) {
            strmsg += ", Password";
            error = true;
        }
        if (MainVoucher.getCompanyName().trim().length() == 0) {
            strmsg += ", Company Name";
            error = true;
        }
        if (vouchercompanyID.getText().toString().trim().length() == 0) {
            strmsg += ", Company ID";
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

    public void ShowAlert(String msg){
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
    public void OnVoucherTaskCompleted(String results) {
        Toast.makeText(getApplicationContext(), "Successs", Toast.LENGTH_SHORT).show();
    }
}
