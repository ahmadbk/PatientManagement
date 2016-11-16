package com.example.ahmad.patientmanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ahmad on 2016/08/17.
 */
public class InsertBackgroundWorker extends AsyncTask<String,Void,String> {

    AlertDialog alertDialog;
    Context context;
    String add_allergy_url = "http://"+StaffLogin.serverAdd+"/AddAllergy.php";
    String add_diagnostics_url = "http://"+StaffLogin.serverAdd+"/AddDiagnostic.php";
    String add_prescription_url = "http://"+StaffLogin.serverAdd+"/AddPrescription.php";
    String add_observation_url = "http://"+StaffLogin.serverAdd+"/AddObservation.php";
    String add_dosage_url = "http://"+StaffLogin.serverAdd+"/AddDosage.php";


    int patient_tag = StaffLogin.patientDetails.getTagID();
    int staff_tag = StaffLogin.staffDetails.getTag();

    InsertBackgroundWorker(Context ctx){
        context = ctx;
    }


    @Override
    protected String doInBackground(String...params) {

        Date dNow = new Date( );
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat curHour = new SimpleDateFormat("HH");

        String justDate = date.format(dNow);
        String timeStamp = timestamp.format(dNow);
        String hour = curHour.format(dNow);
        int current_hour = Integer.parseInt(hour);

        String docName = StaffLogin.staffDetails.getFirst_name() +" "+StaffLogin.staffDetails.getLast_name();


        String insertionType = params[0];

        if(insertionType.equalsIgnoreCase("AddAllergy")) {

            String allergy =params[1];
            Allergies allergies = new Allergies(allergy);
            StaffLogin.patientDetails.addToAllergyArrayList(allergies);
            JSONObject object = new JSONObject();
            try {
                object.put("tag_id",patient_tag);
                object.put("type",allergy);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String dummy = "";
            try {
                dummy = AES.encrypt(object.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }


//---------------------------------------------------------
            try {
                URL url = new URL(add_allergy_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("client_response", "UTF-8") + "=" + URLEncoder.encode("" + dummy, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
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
            Diagnostics diagnostics = new Diagnostics(justDate,docName,description);
            StaffLogin.patientDetails.addToDiagnosticsArrayList(diagnostics);

            JSONObject object = new JSONObject();
            try {
                object.put("tag_id",patient_tag);
                object.put("doctor_tag",staff_tag);
                object.put("description",description);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String dummy = "";
            try {
                dummy = AES.encrypt(object.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                URL url = new URL(add_diagnostics_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("client_response", "UTF-8") + "=" + URLEncoder.encode("" + dummy, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
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
            //Fix These parameters to match yours
            String medicine_name =params[1];
            String qpd = params[2];
            String end_date = params[3];
            String morning = params[4];
            String afternoon = params[5];
            String evening = params[6];
            String mealRelation = params[7];

            Prescriptions prescriptions = new Prescriptions(justDate,end_date,docName,medicine_name,qpd,"Active",morning,afternoon,evening,mealRelation);
            StaffLogin.patientDetails.addToPrescriptionsArrayList(prescriptions);

            JSONObject object = new JSONObject();
            try {
                object.put("tag_id",patient_tag);
                object.put("doctor_tag",staff_tag);
                object.put("medicine_name",medicine_name);
                object.put("quantity_per_dosage",qpd);
                object.put("end_date",end_date);
                object.put("morning",morning);
                object.put("afternoon",afternoon);
                object.put("evening",evening);
                object.put("mealRelation",mealRelation);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String dummy = "";
            try {
                dummy = AES.encrypt(object.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                URL url = new URL(add_prescription_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("client_response", "UTF-8") + "=" + URLEncoder.encode("" + dummy, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
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

            Observations observations = new Observations(timeStamp,docName,bpn,bpd,temperature,pulse,weight);
            StaffLogin.patientDetails.addToObservationsArrayList(observations);

            JSONObject object = new JSONObject();
            try {
                object.put("tag_id",patient_tag);
                object.put("doctor_tag",staff_tag);
                object.put("bpn",bpn);
                object.put("bpd",bpd);
                object.put("temperature",temperature);
                object.put("pulse",pulse);
                object.put("weight",weight);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String dummy = "";
            try {
                dummy = AES.encrypt(object.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                URL url = new URL(add_observation_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("client_response", "UTF-8") + "=" + URLEncoder.encode("" + dummy, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
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
////---------------------------------------------------------
        else if(insertionType.equalsIgnoreCase("AddDosage")){
            int size = StaffLogin.tempDosageArrayList.size();
            for(int i=0;i<size;i++)
            {
                String period = "";
                if(current_hour > -1 && current_hour < 11)
                    period = "morning";
                else if(current_hour > 10 && current_hour < 18)
                    period = "afternoon";
                else if(current_hour > 17 && current_hour < 24)
                    period = "evening";

                Dosage dosage = new Dosage(StaffLogin.staffDetails.getFirst_name()+" "+StaffLogin.staffDetails.getLast_name(),
                        timeStamp,StaffLogin.tempDosageArrayList.get(i).getMedName(),
                        StaffLogin.tempDosageArrayList.get(i).getQuantity(),period);
                StaffLogin.patientDetails.addToDosageArrayList(dosage);

                JSONObject object = new JSONObject();
                try {
                    object.put("tag_id",patient_tag);
                    object.put("nurse_tag",staff_tag);
                    object.put("time",timeStamp);
                    object.put("prescription_id",StaffLogin.tempDosageArrayList.get(i).getPrescription_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String dummy = "";
                try {
                    dummy = AES.encrypt(object.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    URL url = new URL(add_dosage_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("client_response", "UTF-8") + "=" + URLEncoder.encode("" + dummy, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
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
