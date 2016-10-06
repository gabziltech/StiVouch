package com.gabzil.stivouch;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Yogesh on 3/17/2016.
 */
public class SubmitVoucher extends AsyncTask<Object, String, String> {
    private Context mContext;
    ProgressDialog mProgress;
    private OnVoucherTaskCompleted mCallback;

    public SubmitVoucher(Context context, OnVoucherTaskCompleted listner) {
        this.mContext = context;
        this.mCallback = listner;
    }

    @Override
    public void onPreExecute() {
        try {
            mProgress = CreateProgress();
            mProgress.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected String doInBackground(Object[] params) {
        try {
            if (!isCancelled()) {
                VoucherEntities voucher = (VoucherEntities) params[0];
                return getServerInfo(voucher);
            } else
                return null;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String message) {
        mProgress.dismiss();
        mCallback.OnVoucherTaskCompleted(message);
    }

    private ProgressDialog CreateProgress() {
        ProgressDialog cProgress = new ProgressDialog(mContext);
        try {
            String msg = "Please wait...";
//            String title = "Connecting to Internet";
//            SpannableString ss1 = new SpannableString(title);
//            ss1.setSpan(new RelativeSizeSpan(2f), 0, ss1.length(), 0);
//            ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, ss1.length(), 0);

            SpannableString ss2 = new SpannableString(msg);
            ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
            ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss2.length(), 0);

            cProgress.setTitle(null);
            cProgress.setMessage(ss2);
            cProgress.setCanceledOnTouchOutside(false);

            return cProgress;
        } catch (Exception Ex) {
            return cProgress;
        }
    }

    private String getServerInfo(VoucherEntities voucher) {
        Gson gson = new Gson();
        String CustomerString = gson.toJson(voucher);

        StringBuilder urlString = new StringBuilder();
        urlString.append("http://gabstivouch.azurewebsites.net/api/CustomerInfo/customerregister?");
        urlString.append("Cserv=").append(CustomerString);

        HttpURLConnection urlConnection = null;
        URL url = null;
        String temp, response = "";

        try
        {
            url = new URL(urlString.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream inStream = null;
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            while ((temp = bReader.readLine()) != null)
                response += temp;
            bReader.close();
            inStream.close();
            urlConnection.disconnect();
//            object = (JSONObject) new JSONTokener(response).nextValue();
        }
        catch (Exception e)
        {
            e.getMessage();
        }

        return (response);
    }
}
