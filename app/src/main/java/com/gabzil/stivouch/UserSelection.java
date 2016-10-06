package com.gabzil.stivouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserSelection extends Activity {
    private Button voucher,merchant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        voucher=(Button)findViewById(R.id.voucher);
        merchant=(Button)findViewById(R.id.merchant);

        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSelection.this, LoginVoucher.class);
                startActivity(intent);
            }
        });

        merchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

}
