package com.example.ahmad.patientmanagement;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;


public class StaffLogin extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    public static String serverAdd = "192.168.1.66";

    //private NfcAdapter nfcAdapter;
    private NfcAdapter mNfcAdapter;

    String JSON_STRING;
    String jsonPersonal;
    JSONObject jsonObject;
    JSONArray jsonArray;
    public static StaffDetails staffDetails;

    String send;
    String tag_id = "";

    String staff_login_url = "http://"+serverAdd+"/staffLogin.php";
    String get_role_url = "http://"+serverAdd+"/getRole.php";

    public static PatientDetails patientDetails;
    //Change server address here
    public static String medArray[] = new String[100];
    boolean flag = false;
    ArrayList<Diagnostics> diagnosticsArrayList = new ArrayList<Diagnostics>();
    ArrayList<Observations> observationsArrayList = new ArrayList<Observations>();
    ArrayList<Prescriptions> prescriptionsArrayList = new ArrayList<Prescriptions>();
    ArrayList<Allergies> allergiesArrayList = new ArrayList<Allergies>();
    ArrayList<Location> locationArrayList = new ArrayList<Location>();

    String details_url = "http://"+serverAdd+"/viewPatient.php";
    String login_url = "http://"+serverAdd+"/login.php";
    String diagnostics_url = "http://"+serverAdd+"/Diagnostics.php";
    String observations_url = "http://"+serverAdd+"/Observations.php";
    String prescriptions_url = "http://"+serverAdd+"/Prescriptions.php";
    String allergies_url = "http://"+serverAdd+"/Allergies.php";
    String medicine_list_url = "http://"+serverAdd+"/MedicineList.php";
    String locations_url = "http://"+serverAdd+"/Locations.php";

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        //Setting the NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter == null){
            Toast.makeText(this,
                    "NFC NOT supported on this devices!",
                    Toast.LENGTH_LONG).show();
            finish();
        }else if(!mNfcAdapter.isEnabled()){
            Toast.makeText(this,
                    "NFC NOT Enabled!",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        handleIntent(getIntent());

    }

    @Override
    protected void onResume() {
        super.onResume();

        setupForegroundDispatch(this, mNfcAdapter);

    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            System.out.println("ACTION NDEF ");

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask(StaffLogin.this).execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            System.out.println("ACTION TECH ");

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask(StaffLogin.this).execute(tag);
                    break;
                }
            }
        }
    }


    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        Context context;

        public NdefReaderTask(Context ctx){
            context = ctx;
        }
        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            if(tag == null){
            }else{
                int tempH = 0;
                String tagInfo = "";
                byte[] tagId = tag.getId();
                for(int i=0; i<tagId.length; i++){
                    tagInfo = Integer.toHexString(tagId[0] & 0xFF);
                    tempH += Integer.valueOf(tagInfo,16);
                }
                String tempp = Integer.toString(tempH);
                System.out.println(tempp);
                tag_id = tempp;

//------------------------------------------------------------------------------------------------
                //this just checks if the patient exists
                try {

//-------------------------------------------------------------------------------
                    URL url = new URL(get_role_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data = URLEncoder.encode("tag_id","UTF-8")+"="+URLEncoder.encode(tag_id,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();;
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String output="";
                    String line;
                    while((line = bufferedReader.readLine())!=null){
                        output += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

//-------------------------------------------------------------------------------

                    if(output.equalsIgnoreCase("doctor") || output.equalsIgnoreCase("nurse"))
                    {
                        URL url1 = new URL(staff_login_url);
                        HttpURLConnection httpURLConnection1 = (HttpURLConnection) url1.openConnection();
                        httpURLConnection1.setDoOutput(true);
                        httpURLConnection1.setDoInput(true);
                        OutputStream outputStream1 = httpURLConnection1.getOutputStream();
                        BufferedWriter bufferedWriter1 = new BufferedWriter(new OutputStreamWriter(outputStream1, "UTF-8"));
                        String post_data1 = URLEncoder.encode("tag_id", "UTF-8") + "=" + URLEncoder.encode(tag_id, "UTF-8");
                        bufferedWriter1.write(post_data1);
                        bufferedWriter1.flush();
                        ;
                        bufferedWriter1.close();
                        outputStream1.close();
                        InputStream inputStream1 = httpURLConnection1.getInputStream();
                        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1, "iso-8859-1"));
                        StringBuilder stringBuilder = new StringBuilder();
                        JSON_STRING = bufferedReader1.readLine();
                        System.out.println(JSON_STRING);
                        if (!(JSON_STRING.equalsIgnoreCase("failed")))
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
                                while (count < jsonArray.length()) {
                                    JSONObject JO = jsonArray.getJSONObject(count);
                                    role = JO.getString("role");
                                    firstname = JO.getString("first_name");
                                    surname = JO.getString("last_name");
                                    ward = JO.getString("ward_name");
                                    staffDetails = new StaffDetails(Integer.parseInt(tag_id), role, firstname, surname, ward);
                                    count++;
                                }
                                //System.out.println(staffDetails.first_name);
                                if(output.equalsIgnoreCase("doctor"))
                                    return "doctor";
                                else
                                    return "nurse";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if(output.equalsIgnoreCase("patient"))
                    {
                        URL url8 = new URL(details_url);
                        HttpURLConnection httpURLConnection8 = (HttpURLConnection)url8.openConnection();
                        httpURLConnection8.setDoOutput(true);
                        httpURLConnection8.setDoInput(true);
                        OutputStream outputStream8 = httpURLConnection8.getOutputStream();
                        BufferedWriter bufferedWriter8 = new BufferedWriter(new OutputStreamWriter(outputStream8,"UTF-8"));
                        String post_data8 = URLEncoder.encode("tag_id","UTF-8")+"="+URLEncoder.encode(tag_id,"UTF-8");
                        bufferedWriter8.write(post_data8);
                        bufferedWriter8.flush();;
                        bufferedWriter8.close();
                        outputStream8.close();
                        InputStream inputStream8 = httpURLConnection8.getInputStream();
                        BufferedReader bufferedReader8 = new BufferedReader(new InputStreamReader(inputStream8,"iso-8859-1"));
                        StringBuilder stringBuilder8 = new StringBuilder();
                        while((JSON_STRING=bufferedReader8.readLine())!=null)
                        {
                            stringBuilder8.append(JSON_STRING+"\n");
                        }
                        bufferedReader8.close();
                        inputStream8.close();
                        httpURLConnection8.disconnect();
                        jsonPersonal = stringBuilder8.toString().trim();

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

                        return "patient";

//-------------------------------------------------------------------------------------------------------------
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
//------------------------------------------------------------------------------------------------
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            if (!result.equalsIgnoreCase("failed")) {
                if(result.equalsIgnoreCase("patient"))
                {
                    Intent intent = new Intent(context, PatientManager.class);
                    context.startActivity(intent);
                }
                else if(result.equalsIgnoreCase("nurse"))
                {
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }
                else if(result.equalsIgnoreCase("doctor"))
                {
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }

            }
        }
    }

}
