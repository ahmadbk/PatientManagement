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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

public class Login extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private NfcAdapter mNfcAdapter;

    String jsonPersonal;
    JSONObject jsonObject;
    JSONArray jsonArray;

    String tag_id = "";


    ArrayList<Diagnostics> diagnosticsArrayList = new ArrayList<Diagnostics>();
    ArrayList<Observations> observationsArrayList = new ArrayList<Observations>();
    ArrayList<Prescriptions> prescriptionsArrayList = new ArrayList<Prescriptions>();
    ArrayList<Allergies> allergiesArrayList = new ArrayList<Allergies>();
    ArrayList<Location> locationArrayList = new ArrayList<Location>();
    ArrayList<NextDosage> nextDosageArrayList = new ArrayList<NextDosage>();
    ArrayList<Report> reportArrayList = new ArrayList<Report>();
    ArrayList<Dosage> dosageArrayList = new ArrayList<Dosage>();

    String get_role_url = "http://"+StaffLogin.serverAdd+"/getRole.php";
    String details_url = "http://"+StaffLogin.serverAdd+"/viewPatient.php";
    String login_url = "http://"+StaffLogin.serverAdd+"/login.php";
    String diagnostics_url = "http://"+StaffLogin.serverAdd+"/Diagnostics.php";
    String observations_url = "http://"+StaffLogin.serverAdd+"/Observations.php";
    String prescriptions_url = "http://"+StaffLogin.serverAdd+"/Prescriptions.php";
    String allergies_url = "http://"+StaffLogin.serverAdd+"/Allergies.php";
    String medicine_list_url = "http://"+StaffLogin.serverAdd+"/MedicineList.php";
    String locations_url = "http://"+StaffLogin.serverAdd+"/Locations.php";
    String next_dosage_url = "http://"+StaffLogin.serverAdd+"/NextDosage.php";
    String reports_url = "http://"+StaffLogin.serverAdd+"/Reports.php";
    String dosages_url = "http://"+StaffLogin.serverAdd+"/Dosages.php";
    String getTag_url = "http://"+StaffLogin.serverAdd+"/getTagID.php";

    String firstName = "";
    String lastName = "";

    boolean searchByName = false;



    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        if(!StaffLogin.isDoctor){
            EditText firstEdit = (EditText)findViewById(R.id.surnameEditText);
            EditText secondEdit = (EditText)findViewById(R.id.firstNameEditText);
            Button button = (Button)findViewById(R.id.loginButton);

            firstEdit.setVisibility(View.INVISIBLE);
            firstEdit.setEnabled(false);
            secondEdit.setVisibility(View.INVISIBLE);
            secondEdit.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
            button.setEnabled(false);
        }
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

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask(Login.this).execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask(Login.this).execute(tag);
                    break;
                }
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        StaffLogin.isDoctor = false;
        StaffLogin.patientStaff = false;
    }

    public void searchPatientOnClick(View view){

        firstName = ((EditText)findViewById(R.id.firstNameEditText)).getText().toString();
        lastName = ((EditText)findViewById(R.id.surnameEditText)).getText().toString();
        searchByName = true;
        Tag tag = null;
        new NdefReaderTask(Login.this).execute(tag);

    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        Context context;

        public NdefReaderTask(Context ctx){
            context = ctx;
        }
        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            if(!searchByName) {
                Ndef ndef = Ndef.get(tag);
                if (ndef == null) {
                    // NDEF is not supported by this Tag.
                    return null;
                }
            }

                if (tag == null && !searchByName) {}
                else
                {
                    if(!searchByName) {
                        int tempH = 0;
                        String tagInfo = "";
                        byte[] tagId = tag.getId();
                        for (int i = 0; i < tagId.length; i++) {
                            tagInfo = Integer.toHexString(tagId[0] & 0xFF);
                            tempH += Integer.valueOf(tagInfo, 16);
                        }
                        String tempp = Integer.toString(tempH);
                        tag_id = tempp;
                    }
                    else
                    {
                        String data = "";
                        String jsonString = "";

                        System.out.println("First Name: " + firstName);
                        System.out.println("Last Name: " + lastName);

                        try {
                            URL url = new URL(getTag_url);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            OutputStream outputStream = httpURLConnection.getOutputStream();
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                            String post_data = URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(firstName, "UTF-8") + "&"
                                    + URLEncoder.encode("last_name", "UTF-8") + "=" + URLEncoder.encode(lastName, "UTF-8");
                            bufferedWriter.write(post_data);
                            bufferedWriter.flush();
                            bufferedWriter.close();
                            outputStream.close();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                            StringBuilder stringBuilder = new StringBuilder();
                            while ((jsonString = bufferedReader.readLine()) != null) {
                                stringBuilder.append(jsonString + "\n");
                            }
                            bufferedReader.close();
                            inputStream.close();
                            httpURLConnection.disconnect();
                            data = stringBuilder.toString().trim();
                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        tag_id = data;
                        System.out.println("TagID Received: " + tag_id);
                    }


//------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------
                    String output = getData(get_role_url,tag_id);

//-------------------------------------------------------------------------------

                    if(output.equalsIgnoreCase("doctor") || output.equalsIgnoreCase("nurse"))
                    {
                        return "failed";
                    }
                    else if(output.equalsIgnoreCase("patient"))
                    {
                        //Get Basic Details
//----------------------------------------------------------------------------------------------------------------------
                        jsonPersonal = getData(details_url,tag_id);

                        try {
                            jsonObject = new JSONObject(jsonPersonal);
                            jsonArray = jsonObject.getJSONArray("server_response");

                            int count = 0;
                            String firstname, surname, dob, dA, eC,g,pN,a,cname,smoker,alcoholic,image;
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
                                image = JO.getString("image");
                                StaffLogin.patientDetails = new PatientDetails(Integer.parseInt(tag_id), firstname,surname,dob,dA,eC,g,pN,a,cname,smoker,alcoholic,true,image);
                                count++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            return "failed";
                        }

                        String iN = StaffLogin.patientDetails.getImageName();
                        File tempFile = new File("/sdcard/" + iN);

                        try {
                            tempFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        FileDownloader.downloadFile("http://" + StaffLogin.serverAdd + "/" + iN,tempFile);

                        //Get Diagnostics
//----------------------------------------------------------------------------------------------------------------------
                        jsonPersonal = getData(diagnostics_url,tag_id);

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
                            StaffLogin.patientDetails.setDiagnosticsArrayList(diagnosticsArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//----------------------------------------------------------------------------------------------------------------------

                        //Observations
//----------------------------------------------------------------------------------------------------------------------
                        jsonPersonal = getData(observations_url,tag_id);
                        System.out.println(jsonPersonal);

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
                            StaffLogin.patientDetails.setObservationsArrayList(observationsArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//----------------------------------------------------------------------------------------------------------------------

                        //Next Dosage
//----------------------------------------------------------------------------------------------------------------------
                        jsonPersonal = getData(next_dosage_url,tag_id);

                        try {
                            jsonObject = new JSONObject(jsonPersonal);
                            jsonArray = jsonObject.getJSONArray("server_response");

                            int count = 0;
                            String medName,quantity,mealRelation,pID;
                            while (count<jsonArray.length())
                            {
                                JSONObject JO = jsonArray.getJSONObject(count);
                                medName = JO.getString("medName");
                                quantity = JO.getString("quantity_per_dosage");
                                mealRelation = JO.getString("mealRelation");
                                pID = JO.getString("prescription_id");

                                NextDosage nextDosage = new NextDosage(medName,quantity,mealRelation,pID);
                                nextDosageArrayList.add(nextDosage);
                                count++;
                            }
                            StaffLogin.patientDetails.setNextDosageArrayList(nextDosageArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//----------------------------------------------------------------------------------------------------------------------

                        //Prescriptions
//----------------------------------------------------------------------------------------------------------------------
                        jsonPersonal = getData(prescriptions_url,tag_id);

                        try {
                            jsonObject = new JSONObject(jsonPersonal);
                            jsonArray = jsonObject.getJSONArray("server_response");

                            int count = 0;
                            String sD,eD,docFN,docLN,mN,quantity,morning,afternoon,evening,mealRelation,s;
                            while (count<jsonArray.length())
                            {
                                JSONObject JO = jsonArray.getJSONObject(count);
                                sD = JO.getString("start_date");
                                eD = JO.getString("end_date");
                                docFN = JO.getString("doc_first_name");
                                docLN = JO.getString("doc_last_name");
                                mN = JO.getString("medName");
                                quantity = JO.getString("quantity_per_dosage");
                                s = JO.getString("status");
                                morning = JO.getString("morning");
                                afternoon = JO.getString("afternoon");
                                evening = JO.getString("evening");
                                mealRelation = JO.getString("mealRelation");

                                Prescriptions prescriptions = new Prescriptions(sD,eD,docFN+" "+docLN,mN,quantity,s,morning,afternoon,evening,mealRelation);
                                prescriptionsArrayList.add(prescriptions);
                                count++;
                            }
                            StaffLogin.patientDetails.setPrescriptionsArrayList(prescriptionsArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//----------------------------------------------------------------------------------------------------------------------


                        //Allergies
//----------------------------------------------------------------------------------------------------------------------
                        jsonPersonal = getData(allergies_url,tag_id);

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
                            StaffLogin.patientDetails.setAllergiesArrayList(allergiesArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//----------------------------------------------------------------------------------------------------------------------
                        //Medicine List
//----------------------------------------------------------------------------------------------------------------------
                        jsonPersonal = getData(medicine_list_url,tag_id);

                        try {
                            jsonObject = new JSONObject(jsonPersonal);
                            jsonArray = jsonObject.getJSONArray("server_response");

                            int count = 0;
                            String t;
                            while (count<jsonArray.length())
                            {
                                JSONObject JO = jsonArray.getJSONObject(count);
                                t = JO.getString("medicine_name");
                                StaffLogin.medArray[count] = t;
                                System.out.println(t);
                                count++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//----------------------------------------------------------------------------------------------------------------------
                        //Locations
//----------------------------------------------------------------------------------------------------------------------
                        jsonPersonal = getData(locations_url,tag_id);

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
                                Location location = new Location(time_stamp,ward_name);
                                locationArrayList.add(location);
                                count++;
                            }
                            StaffLogin.patientDetails.setLocationArrayList(locationArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//-------------------------------------------------------------------------------------------------------------

                        //Reports
//----------------------------------------------------------------------------------------------------------------------
                        jsonPersonal = getData(reports_url,tag_id);
                        System.out.println(jsonPersonal);

                        try {
                            jsonObject = new JSONObject(jsonPersonal);
                            jsonArray = jsonObject.getJSONArray("server_response");

                            int count = 0;
                            String d,t,nameOfFile;
                            while (count<jsonArray.length())
                            {
                                JSONObject JO = jsonArray.getJSONObject(count);
                                d = JO.getString("date");
                                t = JO.getString("type");
                                nameOfFile = JO.getString("nameOfFile");

                                Report report = new Report(d,t,nameOfFile);
                                reportArrayList.add(report);
                                count++;
                            }
                            StaffLogin.patientDetails.setReportArrayList(reportArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//-------------------------------------------------------------------------------------------------------------
                        //Dosages
//----------------------------------------------------------------------------------------------------------------------
                        jsonPersonal = getData(dosages_url,tag_id);

                        try {
                            jsonObject = new JSONObject(jsonPersonal);
                            jsonArray = jsonObject.getJSONArray("server_response");

                            int count = 0;
                            String nurse,time,medName,quantity,period;
                            while (count<jsonArray.length())
                            {
                                JSONObject JO = jsonArray.getJSONObject(count);
                                nurse = JO.getString("nurse");
                                time = JO.getString("time");
                                medName = JO.getString("medName");
                                quantity = JO.getString("quantity");
                                period = JO.getString("period");

                                Dosage dosage = new Dosage(nurse,time,medName,quantity,period);
                                dosageArrayList.add(dosage);
                                count++;
                            }
                            StaffLogin.patientDetails.setDosageArrayList(dosageArrayList);
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
                diagnosticsArrayList = new ArrayList<Diagnostics>();
                observationsArrayList = new ArrayList<Observations>();
                prescriptionsArrayList = new ArrayList<Prescriptions>();
                allergiesArrayList = new ArrayList<Allergies>();
                locationArrayList = new ArrayList<Location>();
                nextDosageArrayList = new ArrayList<NextDosage>();
                reportArrayList = new ArrayList<Report>();
                dosageArrayList = new ArrayList<Dosage>();

            }
        }

        public String getData(String link,String tag_id)
        {
            String data = "";
            String jsonString = "";
            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("tag_id", "UTF-8") + "=" + URLEncoder.encode(tag_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while ((jsonString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(jsonString + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                data = stringBuilder.toString().trim();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

    }

}
