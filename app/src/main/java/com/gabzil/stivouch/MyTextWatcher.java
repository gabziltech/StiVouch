package com.gabzil.stivouch;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Yogesh on 26-Oct-16.
 */
public class MyTextWatcher implements TextWatcher {
    private EditText et;
    public MyTextWatcher(EditText et) {
        this.et = et;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str = s.toString();
        if (str.length() > 0 && str.contains(" ")) {
            et.setError("Space is not allowed");
            str=str.replace(" ","");
            et.setText(str);
            et.setSelection(str.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
