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
    Button submit;
    BroadcastReceiver receiver;
    Entities e = new Entities();

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

        TextView Resend = (TextView) findViewById(R.id.resend);
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

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.setOTPNo(OtpNumber.getText().toString().trim());
                if(!IsValidation())
                    CallOTPVerification(e.getOTPNo());
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
        if (e.getOTPNo().length() < 4) {
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
            CallOTPVerification(sms1[4]);
        }
    }

    public void CallOTPVerification(String otp) {
        String MobileNo = e.getMobileNo();
        SubmitOTP submit = new SubmitOTP(MobileRegistration.this, this);
        submit.execute(MobileNo,otp);
    }

    @Override
    public void OnTaskCompleted(String results) {
        if (results != "null" && results.length() > 0) {
//            Gson gson = new Gson();
//            CustomerDBEntities customer = gson.fromJson(results, CustomerDBEntities.class);
            if (results.equals("true")) {
                Entities e = new Entities();
                e.setOTP("No");
                e.setLogin("Yes");
                DataHelp dh = new DataHelp(getApplicationContext());
                if (dh.UpdateSelection(e)) {
                    MyOpenHelper m = new MyOpenHelper(getApplicationContext());
                    List<Entities> select = m.getSelections();
                    Intent i = new Intent(MobileRegistration.this, UserSelection.class);
                    startActivity(i);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some problem occured", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Some problem occured", Toast.LENGTH_SHORT).show();
        }
    }
}
