package com.example.ahmad.patientmanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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

/**
 * Created by Dawood on 2016/08/17.
 */
public class InsertBackgroundWorker extends AsyncTask<String,Void,String> {

    AlertDialog alertDialog;
    Context context;
    String add_allergy_url = "http://"+BackgroundWorker.serverAdd+"/AddAllergy.php";
    String add_diagnostics_url = "http://"+BackgroundWorker.serverAdd+"/AddDiagnostic.php";
    String add_prescription_url = "http://"+BackgroundWorker.serverAdd+"/AddPrescription.php";
    String add_observation_url = "http://"+BackgroundWorker.serverAdd+"/AddObservation.php";

    int patient_tag = BackgroundWorker.patientDetails.getTagID();
    int staff_tag = StaffBackgroundWorker.staffDetails.getTag();

    InsertBackgroundWorker(Context ctx){
        context = ctx;
    }


    @Override
    protected String doInBackground(String...params) {

        String insertionType = params[0];

        if(insertionType.equalsIgnoreCase("AddAllergy")) {

            String allergy =params[1];
//---------------------------------------------------------
            try {
                URL url = new URL(add_allergy_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("tag_id", "UTF-8") + "=" + URLEncoder.encode("" + patient_tag, "UTF-8") + "&"
                        + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(allergy, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                ;
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//---------------------------------------------------------
        else if(insertionType.equalsIgnoreCase("AddDiagnostic")){
            String description =params[1];
            try {
                URL url = new URL(add_diagnostics_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("tag_id", "UTF-8") + "=" + URLEncoder.encode("" + patient_tag, "UTF-8") + "&"
                        + URLEncoder.encode("doctor_tag", "UTF-8") + "=" + URLEncoder.encode("" + staff_tag, "UTF-8") + "&"
                        + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                ;
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//---------------------------------------------------------
//---------------------------------------------------------
        else if(insertionType.equalsIgnoreCase("AddPrescription")){
            String medicine_name =params[1];
            String qpd = params[2];
            String fpd = params[3];
            String end_date = params[4];
            try {
                URL url = new URL(add_prescription_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("tag_id", "UTF-8") + "=" + URLEncoder.encode("" + patient_tag, "UTF-8") + "&"
                        + URLEncoder.encode("doctor_tag", "UTF-8") + "=" + URLEncoder.encode("" + staff_tag, "UTF-8") + "&"
                        + URLEncoder.encode("medicine_name", "UTF-8") + "=" + URLEncoder.encode("" + medicine_name, "UTF-8") + "&"
                        + URLEncoder.encode("quantity_per_day", "UTF-8") + "=" + URLEncoder.encode("" + qpd, "UTF-8") + "&"
                        + URLEncoder.encode("frequency_per_day", "UTF-8") + "=" + URLEncoder.encode("" + fpd, "UTF-8") + "&"
                        + URLEncoder.encode("end_date", "UTF-8") + "=" + URLEncoder.encode(end_date, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                ;
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//---------------------------------------------------------
//---------------------------------------------------------
        else if(insertionType.equalsIgnoreCase("AddObservation")){
            String bpn =params[1];
            String bpd = params[2];
            String temperature = params[3];
            String pulse = params[4];
            String weight = params[5];
            try {
                URL url = new URL(add_observation_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("tag_id", "UTF-8") + "=" + URLEncoder.encode("" + patient_tag, "UTF-8") + "&"
                        + URLEncoder.encode("doctor_tag", "UTF-8") + "=" + URLEncoder.encode("" + staff_tag, "UTF-8") + "&"
                        + URLEncoder.encode("bpn", "UTF-8") + "=" + URLEncoder.encode("" + bpn, "UTF-8") + "&"
                        + URLEncoder.encode("bpd", "UTF-8") + "=" + URLEncoder.encode("" + bpd, "UTF-8") + "&"
                        + URLEncoder.encode("temperature", "UTF-8") + "=" + URLEncoder.encode("" + temperature, "UTF-8") + "&"
                        + URLEncoder.encode("pulse", "UTF-8") + "=" + URLEncoder.encode("" + pulse, "UTF-8") + "&"
                        + URLEncoder.encode("weight", "UTF-8") + "=" + URLEncoder.encode(weight, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                ;
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//---------------------------------------------------------


        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context,result,Toast.LENGTH_SHORT).show();

    }
}
