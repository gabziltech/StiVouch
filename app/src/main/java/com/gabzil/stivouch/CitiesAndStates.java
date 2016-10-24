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
public class CitiesAndStates extends AsyncTask<Object, String, String> {
    private Context mContext;
    ProgressDialog mProgress;
    private OnCityTaskCompleted mCallback;

    public CitiesAndStates(Context context, OnCityTaskCompleted listner) {
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
                return getServerInfo();
            } else
                return null;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String message) {
        try {
            mProgress.dismiss();
            mCallback.OnCityTaskCompleted(message);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private ProgressDialog CreateProgress() {
        ProgressDialog cProgress = new ProgressDialog(mContext);
        try {
            String msg = "Please wait...";
            String title = "Information Saving";
            SpannableString ss1 = new SpannableString(title);
            ss1.setSpan(new RelativeSizeSpan(2f), 0, ss1.length(), 0);
            ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, ss1.length(), 0);

            SpannableString ss2 = new SpannableString(msg);
            ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
            ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss2.length(), 0);

            cProgress.setTitle(title);
            cProgress.setMessage(ss2);
            cProgress.setCanceledOnTouchOutside(false);

            return cProgress;
        } catch (Exception Ex) {
            return cProgress;
        }
    }

    private String getServerInfo() {
        String result = "";
        URI uri;
        try {
            uri = new URI("http://gabsti.azurewebsites.net/api/CityApi");
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
//            writer.write();
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
        return "[{\"CityID\":2,\"City\":\"Ahmednagar\"},{\"CityID\":3,\"City\":\"Akola\"},{\"CityID\":4,\"City\":\"Aurangabad\"},{\"CityID\":5,\"City\":\"Dhule\"},{\"CityID\":6,\"City\":\"Jalgaon\"},{\"CityID\":7,\"City\":\"Kolhapur\"},{\"CityID\":8,\"City\":\"Mumbai\"},{\"CityID\":9,\"City\":\"Nagpur\"},{\"CityID\":10,\"City\":\"Nandurbar\"},{\"CityID\":11,\"City\":\"Nashik\"},{\"CityID\":12,\"City\":\"Pune\"},{\"CityID\":1,\"City\":\"Select\"},{\"CityID\":13,\"City\":\"Thane\"},{\"StateID\":2,\"State\":\"Assam\"},{\"StateID\":3,\"State\":\"Bihar\"},{\"StateID\":4,\"State\":\"Delhi\"},{\"StateID\":5,\"State\":\"Goa\"},{\"StateID\":6,\"State\":\"Gujarat\"},{\"StateID\":7,\"State\":\"Kerala\"},{\"StateID\":8,\"State\":\"Madhya Pradesh\"},{\"StateID\":9,\"State\":\"Maharashtra\"},{\"StateID\":10,\"State\":\"Punjab\"},{\"StateID\":11,\"State\":\"Rajasthan\"},{\"StateID\":1,\"State\":\"Select\"}]";
    }
}
