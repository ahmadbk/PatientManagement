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
        String justDate = date.format(dNow);
        String timeStamp = timestamp.format(dNow);
        String docName = StaffLogin.staffDetails.getFirst_name() +" "+StaffLogin.staffDetails.getLast_name();


        String insertionType = params[0];

        if(insertionType.equalsIgnoreCase("AddAllergy")) {

            String allergy =params[1];
            Allergies allergies = new Allergies(allergy);
            StaffLogin.patientDetails.addToAllergyArrayList(allergies);
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
                        + URLEncoder.encode("quantity_per_dosage", "UTF-8") + "=" + URLEncoder.encode("" + qpd, "UTF-8") + "&"
                        + URLEncoder.encode("end_date", "UTF-8") + "=" + URLEncoder.encode(end_date, "UTF-8")+ "&"
                        + URLEncoder.encode("morning", "UTF-8") + "=" + URLEncoder.encode(morning, "UTF-8")+ "&"
                        + URLEncoder.encode("afternoon", "UTF-8") + "=" + URLEncoder.encode(afternoon, "UTF-8")+ "&"
                        + URLEncoder.encode("evening", "UTF-8") + "=" + URLEncoder.encode(evening, "UTF-8")+ "&"
                        + URLEncoder.encode("mealRelation", "UTF-8") + "=" + URLEncoder.encode(mealRelation, "UTF-8");
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
                Dosage dosage = new Dosage(StaffLogin.staffDetails.getFirst_name()+" "+StaffLogin.staffDetails.getLast_name(),
                        timeStamp,StaffLogin.tempDosageArrayList.get(i).getMedName(),
                        StaffLogin.tempDosageArrayList.get(i).getQuantity());
                StaffLogin.patientDetails.addToDosageArrayList(dosage);

                try {
                    URL url = new URL(add_dosage_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("tag_id", "UTF-8") + "=" + URLEncoder.encode("" + patient_tag, "UTF-8") + "&"
                            + URLEncoder.encode("nurse_tag", "UTF-8") + "=" + URLEncoder.encode("" + staff_tag, "UTF-8") + "&"
                            + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(timeStamp, "UTF-8") + "&"
                            + URLEncoder.encode("prescription_id", "UTF-8") + "=" + URLEncoder.encode(StaffLogin.tempDosageArrayList.get(i).getPrescription_id(), "UTF-8");
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
