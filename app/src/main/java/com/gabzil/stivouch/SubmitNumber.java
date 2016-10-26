package com.gabzil.stivouch;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Created by Yogesh on 3/17/2016.
 */
public class SubmitNumber extends AsyncTask<Object, String, String> {
    private Context mContext;
    ProgressDialog mProgress;
    private OnTaskCompleted mCallback;

    public SubmitNumber(Context context, OnTaskCompleted listner) {
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
        if (!isCancelled()) {
            String number = (String) params[0];
            return getServerInfo(number);
        } else
            return null;
    }

    @Override
    protected void onPostExecute(String message) {
        mProgress.dismiss();
        mCallback.OnTaskCompleted(message);
    }

    private ProgressDialog CreateProgress() {
        ProgressDialog cProgress = new ProgressDialog(mContext);
        try {
            String msg = "Please wait...";

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

    private String getServerInfo(String number) {
        String result = "";
        URI uri;
        try {
            //Connect
            uri = new URI("http://gabsti.azurewebsites.net/api/Otp");
            HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            //Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            //Call parserUsuarioJson() inside write(),Make sure it is returning proper json string .
            writer.write(number.toString());
            writer.close();
            outputStream.close();

            //Read
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
