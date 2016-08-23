package com.example.ahmad.patientmanagement;


/**
 * Created by Ahmad on 8/13/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class StaffBackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    String JSON_STRING;
    String jsonPersonal;
    JSONObject jsonObject;
    JSONArray jsonArray;
    public static StaffDetails staffDetails;
    boolean flag = false;

    String send;
    String tag_id = "";
    String staff_login_url = "http://"+BackgroundWorker.serverAdd+"/staffLogin.php";

    StaffBackgroundWorker(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        tag_id = params[0];

            //this just checks if the patient exists
            try {
                    URL url1 = new URL(staff_login_url);
                    HttpURLConnection httpURLConnection1 = (HttpURLConnection)url1.openConnection();
                    httpURLConnection1.setDoOutput(true);
                    httpURLConnection1.setDoInput(true);
                    OutputStream outputStream1 = httpURLConnection1.getOutputStream();
                    BufferedWriter bufferedWriter1 = new BufferedWriter(new OutputStreamWriter(outputStream1,"UTF-8"));
                    String post_data1 = URLEncoder.encode("tag_id","UTF-8")+"="+URLEncoder.encode(tag_id,"UTF-8");
                    bufferedWriter1.write(post_data1);
                    bufferedWriter1.flush();;
                    bufferedWriter1.close();
                    outputStream1.close();
                    InputStream inputStream1 = httpURLConnection1.getInputStream();
                    BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1,"iso-8859-1"));
                    StringBuilder stringBuilder = new StringBuilder();
                    JSON_STRING=bufferedReader1.readLine();
                    System.out.println(JSON_STRING);
                    if(!(JSON_STRING.equalsIgnoreCase("failed")))
                    {
                        stringBuilder.append(JSON_STRING + "\n");

                        bufferedReader1.close();
                        inputStream1.close();
                        httpURLConnection1.disconnect();
                        jsonPersonal = stringBuilder.toString().trim();
                        System.out.println(jsonPersonal);

                        try {
                            jsonObject = new JSONObject(jsonPersonal);
                            jsonArray = jsonObject.getJSONArray("server_response");

                            int count = 0;
                            String role, firstname, surname, ward;
                            while (count < jsonArray.length())
                            {
                                JSONObject JO = jsonArray.getJSONObject(count);
                                role = JO.getString("role");
                                firstname = JO.getString("first_name");
                                surname = JO.getString("last_name");
                                ward = JO.getString("ward_name");
                                staffDetails = new StaffDetails(Integer.parseInt(tag_id), role, firstname, surname, ward);
                                count++;
                            }
                            return "success";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                else
                    {
                        return "failed";
                    }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        if(result.equalsIgnoreCase("success")) {
            Intent intent = new Intent(context, Login.class);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}