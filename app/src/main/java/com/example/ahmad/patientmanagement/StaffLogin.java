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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;


public class StaffLogin extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    public static String serverAdd = "192.168.1.4";
    public static String uniqueNumber= "12345678";
    public static boolean patientStaff = false;
    public static boolean isDoctor = false;
    public static ArrayList<NextDosage> tempDosageArrayList = new ArrayList<NextDosage>();

    //private NfcAdapter nfcAdapter;
    private NfcAdapter mNfcAdapter;

    String jsonPersonal;
    JSONObject jsonObject;
    JSONArray jsonArray;
    public static StaffDetails staffDetails;

    String tag_id = "";

    public static PatientDetails patientDetails;
    //Change server address here
    public static String medArray[] = new String[100];
    boolean flag = false;
    ArrayList<Diagnostics> diagnosticsArrayList = new ArrayList<Diagnostics>();
    ArrayList<Observations> observationsArrayList = new ArrayList<Observations>();
    ArrayList<Prescriptions> prescriptionsArrayList = new ArrayList<Prescriptions>();
    ArrayList<Allergies> allergiesArrayList = new ArrayList<Allergies>();
    ArrayList<Location> locationArrayList = new ArrayList<Location>();
    ArrayList<Report> reportArrayList = new ArrayList<Report>();
    ArrayList<NextDosage> nextDosageArrayList = new ArrayList<NextDosage>();

    String handshake_url = "http://"+serverAdd+"/handshake.php";
    String staff_login_url = "http://"+serverAdd+"/staffLogin.php";
    String get_role_url = "http://"+serverAdd+"/getRole.php";
    String details_url = "http://"+serverAdd+"/viewPatient.php";
    String diagnostics_url = "http://"+serverAdd+"/Diagnostics.php";
    String observations_url = "http://"+serverAdd+"/Observations.php";
    String prescriptions_url = "http://"+serverAdd+"/Prescriptions.php";
    String allergies_url = "http://"+serverAdd+"/Allergies.php";
    String medicine_list_url = "http://"+serverAdd+"/MedicineList.php";
    String locations_url = "http://"+serverAdd+"/Locations.php";
    String reports_url = "http://"+serverAdd+"/Reports.php";
    String next_dosage_url = "http://"+StaffLogin.serverAdd+"/NextDosage.php";



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

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask(StaffLogin.this).execute(tag);

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
                    new NdefReaderTask(StaffLogin.this).execute(tag);
                    break;
                }
            }
        }
    }

    public static void destroyPatient(){
        patientDetails = new PatientDetails();
    }

    public void changeAddressOnClick(View view){
        EditText eText = (EditText)findViewById(R.id.serverAddressEditText);
        String text = eText.getText().toString();
        setAddress(text);

        Toast.makeText(StaffLogin.this,"Server address changed",Toast.LENGTH_LONG).show();
        handshake_url = "http://"+serverAdd+"/handshake.php";
        staff_login_url = "http://"+serverAdd+"/staffLogin.php";
        get_role_url = "http://"+serverAdd+"/getRole.php";
        details_url = "http://"+serverAdd+"/viewPatient.php";
        diagnostics_url = "http://"+serverAdd+"/Diagnostics.php";
        observations_url = "http://"+serverAdd+"/Observations.php";
        prescriptions_url = "http://"+serverAdd+"/Prescriptions.php";
        allergies_url = "http://"+serverAdd+"/Allergies.php";
        medicine_list_url = "http://"+serverAdd+"/MedicineList.php";
        locations_url = "http://"+serverAdd+"/Locations.php";
        reports_url = "http://"+serverAdd+"/Reports.php";
        next_dosage_url = "http://"+StaffLogin.serverAdd+"/NextDosage.php";
    }

    public static void setAddress(String s){
        StaffLogin.serverAdd = s;
        System.out.println(StaffLogin.serverAdd);
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
                tag_id = tempp;
                System.out.println(tag_id);

                diagnosticsArrayList = new ArrayList<Diagnostics>();
                observationsArrayList = new ArrayList<Observations>();
                prescriptionsArrayList = new ArrayList<Prescriptions>();
                allergiesArrayList = new ArrayList<Allergies>();
                locationArrayList = new ArrayList<Location>();
                reportArrayList = new ArrayList<Report>();
                nextDosageArrayList = new ArrayList<NextDosage>();
                patientStaff = false;
                isDoctor = false;
//------------------------------------------------------------------------------------------------

                //Time for a handshake with the server
//-----------------------------------------------------------------------------------------------
                String encryptedUniqueNumber = RSA.encrypt(uniqueNumber);
                String hello1 = getData(handshake_url,encryptedUniqueNumber);
                AES.setKey(RSA.decrypt(hello1));
//------------------------------------------------------------------------------------------------



                    //get Who tagged in first
//-------------------------------------------------------------------------------
                    String output = getData(get_role_url,tag_id);

//-------------------------------------------------------------------------------
                    if(output.equalsIgnoreCase("doctor") || output.equalsIgnoreCase("nurse"))
                    {
                        String temp = getData(staff_login_url,tag_id);
                        try {
                            jsonPersonal = AES.decrypt(temp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                                if(output.equalsIgnoreCase("doctor"))
                                    return "doctor";
                                else
                                    return "nurse";

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    }
                    else if(output.equalsIgnoreCase("patient"))
                    {

                        //Get Basic Details
//----------------------------------------------------------------------------------------------------------------------
                        String dummy = getData(details_url,tag_id);

                        try {
                            jsonPersonal = AES.decrypt(dummy);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                                patientDetails = new PatientDetails(Integer.parseInt(tag_id), firstname,surname,dob,dA,eC,g,pN,a,cname,smoker,alcoholic,true,image);
                                count++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                          dummy = getData(diagnostics_url,tag_id);
                        try {
                            jsonPersonal = AES.decrypt(dummy);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                        dummy = getData(observations_url,tag_id);
                        try {
                            jsonPersonal = AES.decrypt(dummy);
                            System.out.println(jsonPersonal);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


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
                        dummy = getData(prescriptions_url,tag_id);
                        try {
                            jsonPersonal = AES.decrypt(dummy);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
                            patientDetails.setPrescriptionsArrayList(prescriptionsArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//----------------------------------------------------------------------------------------------------------------------

                        //Get Next Dosage
//----------------------------------------------------------------------------------------------------------------------
                        dummy = getData(next_dosage_url,tag_id);
                        try {
                            jsonPersonal = AES.decrypt(dummy);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
                            patientDetails.setNextDosageArrayList(nextDosageArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//----------------------------------------------------------------------------------------------------------------------

                        //Allergies
//----------------------------------------------------------------------------------------------------------------------
                        dummy = getData(allergies_url,tag_id);
                        try {
                            jsonPersonal = AES.decrypt(dummy);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
                        dummy = getData(medicine_list_url,tag_id);
                        try {
                            jsonPersonal = AES.decrypt(dummy);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
                        dummy = getData(locations_url,tag_id);
                        try {
                            jsonPersonal = AES.decrypt(dummy);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
                            patientDetails.setLocationArrayList(locationArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//-------------------------------------------------------------------------------------------------------------

                        //Reports
//----------------------------------------------------------------------------------------------------------------------
                        dummy = getData(reports_url,tag_id);
                        try {
                            jsonPersonal = AES.decrypt(dummy);
                            System.out.println(jsonPersonal);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


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
                            patientDetails.setReportArrayList(reportArrayList);
//                            System.out.println(patientDetails.getReportArrayList().get(0).getLink());
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
                    patientStaff = true;
                    System.out.println("Patient Staff: " + patientStaff);
                    System.out.println("Is Doctor:" + isDoctor);
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
                    isDoctor = true;
                    System.out.println("Is Doctor:" + isDoctor);
                    System.out.println("Patient Staff: " + patientStaff);
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }

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
                Toast.makeText(context,"Could not connect to server",Toast.LENGTH_LONG).show();
            }

            return data;
        }

    }
}
