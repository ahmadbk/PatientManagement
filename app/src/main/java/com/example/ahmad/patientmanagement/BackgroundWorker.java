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
import java.util.ArrayList;

public class BackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    String JSON_STRING;
    String jsonPersonal;
    JSONObject jsonObject;
    JSONArray jsonArray;
    public static PatientDetails patientDetails;
    //Change server address here
    public static String serverAdd = "192.168.1.66";
    public static String medArray[] = new String[100];
    boolean flag = false;
    ArrayList<Diagnostics> diagnosticsArrayList = new ArrayList<Diagnostics>();
    ArrayList<Observations> observationsArrayList = new ArrayList<Observations>();
    ArrayList<Prescriptions> prescriptionsArrayList = new ArrayList<Prescriptions>();
    ArrayList<Allergies> allergiesArrayList = new ArrayList<Allergies>();
    ArrayList<Location> locationArrayList = new ArrayList<Location>();

    String send;
    String tag_id = "";
    String details_url = "http://"+serverAdd+"/viewPatient.php";
    String login_url = "http://"+serverAdd+"/login.php";
    String diagnostics_url = "http://"+serverAdd+"/Diagnostics.php";
    String observations_url = "http://"+serverAdd+"/Observations.php";
    String prescriptions_url = "http://"+serverAdd+"/Prescriptions.php";
    String allergies_url = "http://"+serverAdd+"/Allergies.php";
    String medicine_list_url = "http://"+serverAdd+"/MedicineList.php";
    String locations_url = "http://"+serverAdd+"/Locations.php";



    BackgroundWorker(Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        tag_id = params[1];
        String type = params[0];

        if(type.equals("patient")){
            //this just checks if the patient exists
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("tag_id","UTF-8")+"="+URLEncoder.encode(tag_id,"UTF-8")+"&"
                        + URLEncoder.encode("role","UTF-8")+"="+URLEncoder.encode(type,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();;
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line;
                while((line = bufferedReader.readLine())!=null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //if the patient exists, than u get patient details
                if(result.equalsIgnoreCase("success"))
                {
                    flag = true;
                    URL url1 = new URL(details_url);
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
                    while((JSON_STRING=bufferedReader1.readLine())!=null)
                    {
                        stringBuilder.append(JSON_STRING+"\n");
                    }
                    bufferedReader1.close();
                    inputStream1.close();
                    httpURLConnection1.disconnect();
                    jsonPersonal = stringBuilder.toString().trim();

                    try {
                        jsonObject = new JSONObject(jsonPersonal);
                        jsonArray = jsonObject.getJSONArray("server_response");

                        int count = 0;
                        String firstname, surname, dob, dA, eC,g,pN,a,cname,smoker,alcoholic;
                        while (count<jsonArray.length())
                        {
                            JSONObject JO = jsonArray.getJSONObject(count);
                            firstname = JO.getString("first_name");
                            surname = JO.getString("last_name");
                            dob = JO.getString("id_num");
                            dA = JO.getString("admission_date");
                            eC = JO.getString("emergency_num");
                            g = JO.getString("gender");
                            pN = JO.getString("phone_num");
                            a = JO.getString("address");
                            cname = JO.getString("current_ward");
                            smoker = JO.getString("smoker");
                            alcoholic = JO.getString("alcoholic");
                            patientDetails = new PatientDetails(Integer.parseInt(tag_id), firstname,surname,dob,dA,eC,g,pN,a,cname,smoker,alcoholic);
                            count++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Get Diagnostics
//----------------------------------------------------------------------------------------------------------------------
                    URL url2 = new URL(diagnostics_url);
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection)url2.openConnection();
                    httpURLConnection2.setDoOutput(true);
                    httpURLConnection2.setDoInput(true);
                    OutputStream outputStream2 = httpURLConnection2.getOutputStream();
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(outputStream2,"UTF-8"));
                    String post_data2 = URLEncoder.encode("tag_id","UTF-8")+"="+URLEncoder.encode(tag_id,"UTF-8");
                    bufferedWriter2.write(post_data2);
                    bufferedWriter2.flush();;
                    bufferedWriter2.close();
                    outputStream2.close();
                    InputStream inputStream2 = httpURLConnection2.getInputStream();
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2,"iso-8859-1"));
                    StringBuilder stringBuilder2 = new StringBuilder();
                    while((JSON_STRING=bufferedReader2.readLine())!=null)
                    {
                        stringBuilder2.append(JSON_STRING+"\n");
                    }
                    bufferedReader2.close();
                    inputStream2.close();
                    httpURLConnection2.disconnect();
                    jsonPersonal = stringBuilder2.toString().trim();

                    try {
                        jsonObject = new JSONObject(jsonPersonal);
                        jsonArray = jsonObject.getJSONArray("server_response");

                        int count = 0;
                        String date, docFN,docLN,descrip;
                        while (count<jsonArray.length())
                        {
                            JSONObject JO = jsonArray.getJSONObject(count);
                            date = JO.getString("date");
                            docFN = JO.getString("doc_first_name");
                            docLN = JO.getString("doc_last_name");
                            descrip = JO.getString("description");
                            Diagnostics diagnostics = new Diagnostics(date,docFN+" "+docLN,descrip);
                            diagnosticsArrayList.add(diagnostics);
                            count++;
                        }
                        patientDetails.setDiagnosticsArrayList(diagnosticsArrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//----------------------------------------------------------------------------------------------------------------------

                    //Observations
//----------------------------------------------------------------------------------------------------------------------
                    URL url3 = new URL(observations_url);
                    HttpURLConnection httpURLConnection3 = (HttpURLConnection)url3.openConnection();
                    httpURLConnection3.setDoOutput(true);
                    httpURLConnection3.setDoInput(true);
                    OutputStream outputStream3 = httpURLConnection3.getOutputStream();
                    BufferedWriter bufferedWriter3 = new BufferedWriter(new OutputStreamWriter(outputStream3,"UTF-8"));
                    String post_data3 = URLEncoder.encode("tag_id","UTF-8")+"="+URLEncoder.encode(tag_id,"UTF-8");
                    bufferedWriter3.write(post_data3);
                    bufferedWriter3.flush();;
                    bufferedWriter3.close();
                    outputStream3.close();
                    InputStream inputStream3 = httpURLConnection3.getInputStream();
                    BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputStream3,"iso-8859-1"));
                    StringBuilder stringBuilder3 = new StringBuilder();
                    while((JSON_STRING=bufferedReader3.readLine())!=null)
                    {
                        stringBuilder3.append(JSON_STRING+"\n");
                    }
                    bufferedReader3.close();
                    inputStream3.close();
                    httpURLConnection3.disconnect();
                    jsonPersonal = stringBuilder3.toString().trim();

                    try {
                        jsonObject = new JSONObject(jsonPersonal);
                        jsonArray = jsonObject.getJSONArray("server_response");

                        int count = 0;
                        String date, docFN,docLN,bpn,bpd,temp,pulse,weight;
                        while (count<jsonArray.length())
                        {
                            JSONObject JO = jsonArray.getJSONObject(count);
                            date = JO.getString("date");
                            docFN = JO.getString("doc_first_name");
                            docLN = JO.getString("doc_last_name");
                            bpn = JO.getString("bpn");
                            bpd = JO.getString("bpd");
                            temp = JO.getString("temperature");
                            pulse = JO.getString("pulse");
                            weight = JO.getString("weight");

                            Observations observations = new Observations(date,docFN+" "+docLN,bpn,bpd,temp,pulse,weight);
                            observationsArrayList.add(observations);
                            count++;
                        }
                        patientDetails.setObservationsArrayList(observationsArrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//----------------------------------------------------------------------------------------------------------------------

                    //Prescriptions
//----------------------------------------------------------------------------------------------------------------------
                    URL url4 = new URL(prescriptions_url);
                    HttpURLConnection httpURLConnection4 = (HttpURLConnection)url4.openConnection();
                    httpURLConnection4.setDoOutput(true);
                    httpURLConnection4.setDoInput(true);
                    OutputStream outputStream4 = httpURLConnection4.getOutputStream();
                    BufferedWriter bufferedWriter4 = new BufferedWriter(new OutputStreamWriter(outputStream4,"UTF-8"));
                    String post_data4 = URLEncoder.encode("tag_id","UTF-8")+"="+URLEncoder.encode(tag_id,"UTF-8");
                    bufferedWriter4.write(post_data4);
                    bufferedWriter4.flush();;
                    bufferedWriter4.close();
                    outputStream4.close();
                    InputStream inputStream4 = httpURLConnection4.getInputStream();
                    BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(inputStream4,"iso-8859-1"));
                    StringBuilder stringBuilder4 = new StringBuilder();
                    while((JSON_STRING=bufferedReader4.readLine())!=null)
                    {
                        stringBuilder4.append(JSON_STRING+"\n");
                    }
                    bufferedReader4.close();
                    inputStream4.close();
                    httpURLConnection4.disconnect();
                    jsonPersonal = stringBuilder4.toString().trim();

                    try {
                        jsonObject = new JSONObject(jsonPersonal);
                        jsonArray = jsonObject.getJSONArray("server_response");

                        int count = 0;
                        String sD,eD,docFN,docLN,mN,q,f,s;
                        while (count<jsonArray.length())
                        {
                            JSONObject JO = jsonArray.getJSONObject(count);
                            sD = JO.getString("start_date");
                            eD = JO.getString("end_date");
                            docFN = JO.getString("doc_first_name");
                            docLN = JO.getString("doc_last_name");
                            mN = JO.getString("medName");
                            q = JO.getString("quantity_per_day");
                            f = JO.getString("frequency_per_day");
                            s = JO.getString("status");

                            Prescriptions prescriptions = new Prescriptions(sD,eD,docFN+" "+docLN,mN,q,f,s);
                            prescriptionsArrayList.add(prescriptions);
                            count++;
                        }
                        patientDetails.setPrescriptionsArrayList(prescriptionsArrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//----------------------------------------------------------------------------------------------------------------------


                    //Allergies
//----------------------------------------------------------------------------------------------------------------------
                    URL url5 = new URL(allergies_url);
                    HttpURLConnection httpURLConnection5 = (HttpURLConnection)url5.openConnection();
                    httpURLConnection5.setDoOutput(true);
                    httpURLConnection5.setDoInput(true);
                    OutputStream outputStream5 = httpURLConnection5.getOutputStream();
                    BufferedWriter bufferedWriter5 = new BufferedWriter(new OutputStreamWriter(outputStream5,"UTF-8"));
                    String post_data5 = URLEncoder.encode("tag_id","UTF-8")+"="+URLEncoder.encode(tag_id,"UTF-8");
                    bufferedWriter5.write(post_data5);
                    bufferedWriter5.flush();;
                    bufferedWriter5.close();
                    outputStream5.close();
                    InputStream inputStream5 = httpURLConnection5.getInputStream();
                    BufferedReader bufferedReader5 = new BufferedReader(new InputStreamReader(inputStream5,"iso-8859-1"));
                    StringBuilder stringBuilder5 = new StringBuilder();
                    while((JSON_STRING=bufferedReader5.readLine())!=null)
                    {
                        stringBuilder5.append(JSON_STRING+"\n");
                    }
                    bufferedReader5.close();
                    inputStream5.close();
                    httpURLConnection5.disconnect();
                    jsonPersonal = stringBuilder5.toString().trim();

                    try {
                        jsonObject = new JSONObject(jsonPersonal);
                        jsonArray = jsonObject.getJSONArray("server_response");

                        int count = 0;
                        String t;
                        while (count<jsonArray.length())
                        {
                            JSONObject JO = jsonArray.getJSONObject(count);
                            t = JO.getString("type");

                            Allergies allergies = new Allergies(t);

                            allergiesArrayList.add(allergies);
                            count++;
                        }
                        patientDetails.setAllergiesArrayList(allergiesArrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//----------------------------------------------------------------------------------------------------------------------
                    //Medicine List
//----------------------------------------------------------------------------------------------------------------------
                    URL url6 = new URL(medicine_list_url);
                    HttpURLConnection httpURLConnection6 = (HttpURLConnection)url6.openConnection();
                    httpURLConnection6.setDoOutput(true);
                    httpURLConnection6.setDoInput(true);
                    //OutputStream outputStream6 = httpURLConnection6.getOutputStream();
                    //BufferedWriter bufferedWriter5 = new BufferedWriter(new OutputStreamWriter(outputStream5,"UTF-8"));
                    //String post_data5 = URLEncoder.encode("tag_id","UTF-8")+"="+URLEncoder.encode(tag_id,"UTF-8");
                    //bufferedWriter5.write(post_data4);
                    //bufferedWriter5.flush();;
                    //bufferedWriter5.close();
                    //outputStream5.close();
                    InputStream inputStream6 = httpURLConnection6.getInputStream();
                    BufferedReader bufferedReader6 = new BufferedReader(new InputStreamReader(inputStream6,"iso-8859-1"));
                    StringBuilder stringBuilder6 = new StringBuilder();
                    while((JSON_STRING=bufferedReader6.readLine())!=null)
                    {
                        stringBuilder6.append(JSON_STRING+"\n");
                    }
                    bufferedReader6.close();
                    inputStream6.close();
                    httpURLConnection6.disconnect();
                    jsonPersonal = stringBuilder6.toString().trim();

                    try {
                        jsonObject = new JSONObject(jsonPersonal);
                        jsonArray = jsonObject.getJSONArray("server_response");

                        int count = 0;
                        String t;
                        while (count<jsonArray.length())
                        {
                            JSONObject JO = jsonArray.getJSONObject(count);
                            t = JO.getString("medicine_name");
                            medArray[count] = t;
                            System.out.println(t);
                            count++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//----------------------------------------------------------------------------------------------------------------------
                    //Locations
//----------------------------------------------------------------------------------------------------------------------
                    URL url7 = new URL(locations_url);
                    HttpURLConnection httpURLConnection7 = (HttpURLConnection)url7.openConnection();
                    httpURLConnection7.setDoOutput(true);
                    httpURLConnection7.setDoInput(true);
                    OutputStream outputStream7 = httpURLConnection7.getOutputStream();
                    BufferedWriter bufferedWriter7 = new BufferedWriter(new OutputStreamWriter(outputStream7,"UTF-8"));
                    String post_data7 = URLEncoder.encode("tag_id","UTF-8")+"="+URLEncoder.encode(tag_id,"UTF-8");
                    bufferedWriter7.write(post_data7);
                    bufferedWriter7.flush();;
                    bufferedWriter7.close();
                    outputStream7.close();
                    InputStream inputStream7 = httpURLConnection7.getInputStream();
                    BufferedReader bufferedReader7 = new BufferedReader(new InputStreamReader(inputStream7,"iso-8859-1"));
                    StringBuilder stringBuilder7 = new StringBuilder();
                    while((JSON_STRING=bufferedReader7.readLine())!=null)
                    {
                        stringBuilder7.append(JSON_STRING+"\n");
                    }
                    bufferedReader7.close();
                    inputStream7.close();
                    httpURLConnection7.disconnect();
                    jsonPersonal = stringBuilder7.toString().trim();

                    try {
                        jsonObject = new JSONObject(jsonPersonal);
                        jsonArray = jsonObject.getJSONArray("server_response");

                        int count = 0;
                        String time_stamp,ward_name;
                        while (count<jsonArray.length())
                        {
                            JSONObject JO = jsonArray.getJSONObject(count);
                            time_stamp = JO.getString("time_stamp");
                            ward_name = JO.getString("ward_name");
                            System.out.println(ward_name);
                            Location location = new Location(time_stamp,ward_name);

                            locationArrayList.add(location);
                            count++;
                        }
                        patientDetails.setLocationArrayList(locationArrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//----------------------------------------------------------------------------------------------------------------------
                }
                else
                {
                    return result;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        if(flag) {
            send = result;
            Intent intent = new Intent(context, PatientManager.class);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}