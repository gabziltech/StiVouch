package com.gabzil.stivouch;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MobileRegistration extends Activity implements OnTaskCompleted {
    EditText OtpNumber;
    Button submit;
    BroadcastReceiver receiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_registration);

        OtpNumber = (EditText) findViewById(R.id.otp);
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

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IsValidation())
                    CallOTPVerification(OtpNumber.getText().toString().trim());
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

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private boolean IsValidation() {
        boolean error = false;
        if (OtpNumber.getText().toString().trim().length() < 4) {
            Toast.makeText(getApplicationContext(), "Please enter 4 digits OTP", Toast.LENGTH_SHORT).show();
            error = true;
        }
        return error;
    }

    public void processReceive (Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        String smsBody,sms = "";
        SmsMessage msgs = null;
        String senderNumber;

        for(int i=0; i<pdus.length; i++){
            msgs = SmsMessage.createFromPdu((byte[]) pdus[i]);

            smsBody = msgs.getMessageBody();
            senderNumber = msgs.getOriginatingAddress();
            sms += "From: " +senderNumber+ "\nContent: "+smsBody+ "\n";
        }
        String[] sms1 = sms.split("\n");
        OtpNumber.setText(sms1[2]);
        CallOTPVerification(sms1[2]);
    }

    public void CallOTPVerification(String otp) {
        SubmitNumber submit = new SubmitNumber(MobileRegistration.this, this);
        submit.execute(otp);
    }

    @Override
    public void OnTaskCompleted(String results) {
        if (results != "null" && results.length() > 0){
//            Gson gson = new Gson();
//            CustomerDBEntities customer = gson.fromJson(results, CustomerDBEntities.class);
            if (results.equals("true")){
                Intent i = new Intent(MobileRegistration.this,SetPassword.class);
                startActivity(i);
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Some problem occured",Toast.LENGTH_SHORT).show();
        }
    }
}
