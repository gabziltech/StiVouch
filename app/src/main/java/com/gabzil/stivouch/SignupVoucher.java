package com.gabzil.stivouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    EditText Vouchername, Username, Password, ConfirmPassword, MobileNo, EMailID, CompanyName, CompanyID;
    Spinner City, State;
    Button CreateAccount;
    VoucherEntities MainVoucher;
    TextView DOB;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_voucher);

        DeclareCustomerVariables();
        SimpleDateFormat myFormat = new SimpleDateFormat("MMM dd, yyyy");
        String currentDateTimeString = myFormat.format(new Date());
        DOB.setText(currentDateTimeString.substring(0, 12));
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetDate();
            }
        });

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVoucherInfo();
                if (!IsValidation()) {
                    SaveVoucherData();
                }
            }
        });
    }

    public void SetDate() {
        new DatePickerDialog(this, date1, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        DOB.setText(df.format(myCalendar.getTime()));
    }

    public void DeclareCustomerVariables() {
        Vouchername = (EditText) findViewById(R.id.vouchername);
        Username = (EditText) findViewById(R.id.v_username);
        Password = (EditText) findViewById(R.id.v_pass);
        ConfirmPassword = (EditText) findViewById(R.id.v_cnfrmpass);
        DOB = (TextView) findViewById(R.id.v_dob);
        MobileNo = (EditText) findViewById(R.id.v_mobileno);
        EMailID = (EditText) findViewById(R.id.v_mail_id);
        CompanyName = (EditText) findViewById(R.id.v_companyname);
        CompanyID = (EditText) findViewById(R.id.v_companyid);
        City = (Spinner) findViewById(R.id.v_city);
        State = (Spinner) findViewById(R.id.v_state);
        CreateAccount = (Button) findViewById(R.id.v_registration);
        myCalendar = Calendar.getInstance();
        MainVoucher = new VoucherEntities();
    }

    public void setVoucherInfo() {
        try {
            MainVoucher.setVoucherName(Vouchername.getText().toString().trim());
            MainVoucher.setUserName(Username.getText().toString().trim());
            MainVoucher.setPassword(Password.getText().toString().trim());
            MainVoucher.setDOB(DOB.getText().toString().trim());
            MainVoucher.setMobileNo(MobileNo.getText().toString().trim());
            MainVoucher.setCity(City.getSelectedItem().toString().trim());
            MainVoucher.setState(State.getSelectedItem().toString().trim());
            MainVoucher.setEMailID(EMailID.getText().toString().trim());
            MainVoucher.setCompanyName(CompanyName.getText().toString().trim());
            MainVoucher.setCompanyID(Integer.parseInt(CompanyID.getText().toString().trim()));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private boolean IsValidation() {
        boolean error = false;
        String strmsg = "Please Enter ";

        if (MainVoucher.getVoucherName().length() == 0) {
            strmsg += "Voucher Name";
            error = true;
        }
        if (MainVoucher.getUserName().length() == 0) {
            strmsg += ", Username";
            error = true;
        }
        if (MainVoucher.getPassword().length() == 0) {
            strmsg += ", Password";
            error = true;
        } else if (MainVoucher.getPassword().length() < 6) {
            strmsg += ", Password of minimum 6 characters";
            error = true;
        } else if (ConfirmPassword.getText().toString().trim().length() == 0) {
            strmsg += ", Confirm Password";
            error = true;
        } else if (!MainVoucher.getPassword().equals(ConfirmPassword.getText().toString().trim())) {
            strmsg += ", Password & Confirm Password Same";
            error = true;
        }
        if (MainVoucher.getDOB().length() == 0) {
            strmsg += ", DOB";
            error = true;
        }
        if (MainVoucher.getMobileNo().length() == 0) {
            strmsg += ", Mobile No";
            error = true;
        } else if (MainVoucher.getMobileNo().length() < 10) {
            strmsg += ", 10 digit Mobile No";
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
        if (MainVoucher.getEMailID().length() == 0) {
            strmsg += ", Email ID";
            error = true;
        } else if (!isValidEmail(MainVoucher.getEMailID())) {
            strmsg += ", Valid Email ID";
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

    private void SaveVoucherData() {
        SubmitVoucher p = new SubmitVoucher(SignupVoucher.this, this);
        p.execute(MainVoucher);
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
    public void OnVoucherTaskCompleted(String results) {
        try {
            if (results != "null" && results.length() > 0) {
                if (results.equals("true")) {
                        Intent i = new Intent(SignupVoucher.this, Login.class);
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
}
