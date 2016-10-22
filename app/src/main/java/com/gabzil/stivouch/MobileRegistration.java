package com.gabzil.stivouch;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MobileRegistration extends Activity implements OnTaskCompleted {
    EditText OtpNumber;
    TextView MobileNo;
    Button Resend,submit;
    BroadcastReceiver receiver;
    MyOpenHelper m;
    OTPInfo info = new OTPInfo();
    List<Entities> select;
    String Mobileno;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_registration);

        DeclareVariables();

        MobileNo.setText("+91-" + Mobileno);
        info.setMobileNo(Mobileno);

        OtpNumber.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit.performClick();
                    return true;
                }
                return false;
            }
        });

        Resend.setPaintFlags(Resend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelp dh = new DataHelp(getApplicationContext());
                dh.DeleteSelection();
                Intent i = new Intent(MobileRegistration.this, LandingPage.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setOTPNo(Integer.parseInt(OtpNumber.getText().toString().trim()));
                if(!IsValidation())
                    CallOTPVerification();
            }
        });

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arr0, Intent arr1) {
                processReceive(arr0,arr1);
            }
        };
        registerReceiver(receiver, filter);
    }

    public void DeclareVariables() {
        MobileNo = (TextView) findViewById(R.id.mobileno);
        OtpNumber = (EditText) findViewById(R.id.otp);
        Resend = (Button) findViewById(R.id.resend);
        submit = (Button) findViewById(R.id.submit);
        m = new MyOpenHelper(getApplicationContext());
        select = m.getSelections();
        Mobileno = select.get(0).getMobileNo();
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private boolean IsValidation() {
        boolean error = false;

        if (OtpNumber.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
            error = true;
        } else if (OtpNumber.getText().toString().trim().length() < 4) {
            Toast.makeText(getApplicationContext(), "Please enter 4 digits OTP", Toast.LENGTH_SHORT).show();
            error = true;
        }
        return error;
    }

    public void processReceive (Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        String smsBody = null;
        SmsMessage msgs = null;
        String senderNumber = null;

        for(int i=0; i<pdus.length; i++){
            msgs = SmsMessage.createFromPdu((byte[]) pdus[i]);
            smsBody = msgs.getMessageBody();
            senderNumber = msgs.getOriginatingAddress();
        }
        String[] sender = senderNumber.split("-");
        if (sender[1].equals("040060")) {
            String[] sms1 = smsBody.split(" ");
            OtpNumber.setText(sms1[4]);
            info.setOTPNo(Integer.parseInt(sms1[4]));
            CallOTPVerification();
        }
    }

    public void CallOTPVerification() {
        SubmitOTP submit = new SubmitOTP(MobileRegistration.this, this);
        submit.execute(info);
    }

    @Override
    public void OnTaskCompleted(String results) {
        try {
            if (results != "null" && results.length() > 0) {
                if (results.equals("true")) {
                    Entities e = new Entities();
                    e.setMobileNo(Mobileno);
                    e.setOTP("No");
                    e.setPin("Yes");
                    e.setLogin("No");
                    DataHelp dh = new DataHelp(getApplicationContext());
                    if (dh.UpdateSelection(e)) {
                        Intent i = new Intent(MobileRegistration.this, SetPin.class);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some problem occured", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error:" +e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
