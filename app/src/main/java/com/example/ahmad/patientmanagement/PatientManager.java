package com.example.ahmad.patientmanagement;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class PatientManager extends AppCompatActivity {
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    DatePickerFragment mDialogFragment;
    private NfcAdapter mNfcAdapter;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    PatientDetails patientDetails;
    private boolean tempDateSet = false;
    private String tempDate = "";
    private boolean isPDF = false;
    String getFile_url = "http://"+StaffLogin.serverAdd+"/EncryptFile.php";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionThread sessionThread = new SessionThread();
        sessionThread.execute();

        setContentView(R.layout.activity_patient_manager);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if(!StaffLogin.patientStaff) {
            if (StaffLogin.isDoctor)
                toolbar.setBackgroundResource(R.color.maroon);
            else
                toolbar.setBackgroundResource(R.color.green);
            toolbar.setSubtitle("Logged in as " + StaffLogin.staffDetails.getFirst_name() + " " + StaffLogin.staffDetails.getLast_name());
        }
        else
            toolbar.setSubtitle("Logged in as " + StaffLogin.patientDetails.getFirstname() + " " + StaffLogin.patientDetails.getSurname());

        /**
         *Setup the DrawerLayout and NavigationView
         */


        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
        if (findViewById(R.id.patientView) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            PatientInfo firstFragment = new PatientInfo();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.patientView, firstFragment).commit();
        }
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment tabFrags = new TabFragment();
        mFragmentTransaction.replace(R.id.containerView, tabFrags).commit();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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

        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

        }
    }


    public void setDate(View v) {
        mDialogFragment = new DatePickerFragment();
        mDialogFragment.show(getSupportFragmentManager(), "timePicker");
    }

    String insertionType = "";

    public void addAllergyButtonClick(View view) {
        EditText editText = (EditText) findViewById(R.id.allergyEditBox);
        insertionType = "AddAllergy";
        String allergy = editText.getText().toString();

        InsertBackgroundWorker insertBackgroundWorker = new InsertBackgroundWorker(this);
        insertBackgroundWorker.execute(insertionType, allergy);

    }

    public void addDiagnosticButtonClick(View view) {
        EditText notesEdit = (EditText) findViewById(R.id.notesEditText);
        String notes = notesEdit.getText().toString();


        InsertBackgroundWorker insertBackgroundWorker = new InsertBackgroundWorker(this);
        insertBackgroundWorker.execute("AddDiagnostic", notes);
    }

    public void addPrescriptionButtonClick(View view) {
        Spinner medsSpinner = (Spinner) findViewById(R.id.spinner);
        String medicine = medsSpinner.getSelectedItem().toString();

        EditText quanEdit = (EditText) findViewById(R.id.dosageQuantity);
        String quantity = quanEdit.getText().toString();

        String isMorning = ((CheckBox) findViewById(R.id.addMorningCheckBox)).isChecked() ? "true" : "false";
        String isAfternoon = ((CheckBox) findViewById(R.id.addAfternoonCheckBox)).isChecked() ? "true" : "false";
        String isEvening = ((CheckBox) findViewById(R.id.addEveningCheckBox)).isChecked() ? "true" : "false";

        Spinner mealSpinner = (Spinner) findViewById(R.id.mealRelationSpinner);
        String mealRelation = mealSpinner.getSelectedItemPosition() == 0 ? "BeforeMeal" : "AfterMeal";

        if (!tempDateSet) {
            Toast.makeText(getBaseContext(), "Enter date", Toast.LENGTH_SHORT).show();
            return;
        }

        InsertBackgroundWorker insertBackgroundWorker = new InsertBackgroundWorker(this);
        insertBackgroundWorker.execute("AddPrescription", medicine, quantity, tempDate, isMorning, isAfternoon, isEvening, mealRelation);
        tempDateSet = false;
    }

    public void addObservationButtonCLick(View view) {
        String bpn = ((SeekBar) findViewById(R.id.seekBar1)).getProgress() + "";
        String bpd = ((SeekBar) findViewById(R.id.seekBar2)).getProgress() + "";
        String temp = ((SeekBar) findViewById(R.id.seekBar3)).getProgress() + "";
        String pulse = ((SeekBar) findViewById(R.id.seekBar4)).getProgress() + "";
        String weight = ((SeekBar) findViewById(R.id.seekBar5)).getProgress() + "";

        InsertBackgroundWorker insertBackgroundWorker = new InsertBackgroundWorker(this);
        insertBackgroundWorker.execute("AddObservation", bpn, bpd, temp, pulse, weight);
    }

    public void setTempDate(String date) {
        tempDate = date;
        tempDateSet = true;
    }

    public void nextDosageSubmitOnClick(View view) {
        int size = StaffLogin.patientDetails.getNextDosageArrayList().size();
        int j = 0;
        for (int i = 0; i < size; i++) {
            CheckBox box = (CheckBox)findViewById(NextDosageListerFragment.getCheckBoxId(i));
            if(box.isChecked()){
                StaffLogin.tempDosageArrayList.add(StaffLogin.patientDetails.getNextDosageArrayList().get(j));
                StaffLogin.patientDetails.removeFromNextDosageArrayList(j);
                j--;
            }
            j++;
        }
        InsertBackgroundWorker insertBackgroundWorker = new InsertBackgroundWorker(this);
        insertBackgroundWorker.execute("AddDosage");
    }

    public void openFileButtonClick(View view) {
        String fileName = ((Spinner) findViewById(R.id.fileSpinner)).getSelectedItem().toString();
        String ttt = "";
        String dummy = "";

        for(int i =0;i<StaffLogin.patientDetails.getReportArrayList().size();i++)
        {
            String fN = StaffLogin.patientDetails.getReportArrayList().get(i).getType();
            if(fN.equalsIgnoreCase(fileName))
            {
                String typeOfFile = StaffLogin.patientDetails.getReportArrayList().get(i).getType();
                String tagNum = ""+StaffLogin.patientDetails.getTagID();

                JSONObject object = new JSONObject();
                try {
                    object.put("tag_id",tagNum);
                    object.put("typeOfFile",typeOfFile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    dummy = AES.encrypt(object.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ttt = StaffLogin.patientDetails.getReportArrayList().get(i).getNameOfFile();
                String docType = ttt.substring(ttt.length()-3);
                if(docType.equalsIgnoreCase("pdf"))isPDF = true;
                else isPDF = false;
            }
        }
        new DownloadFile().execute(getFile_url, "/sdcard/" + ttt,dummy);
    }

    public void destroy()
    {
        onDestroy();
        //Intent intent = new Intent(PatientManager.this, Login.class);
        //PatientManager.this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        int size = StaffLogin.patientDetails.getReportArrayList().size();
        for (int i = 0; i < size; i++) {
            String fileName = StaffLogin.patientDetails.getReportArrayList().get(i).getNameOfFile();
            File file = new File("/sdcard/" + fileName);
            if (file.exists()) {
                if (file.delete())
                    System.out.println(file.getAbsolutePath() + " deleted");
            }
        }

        File file = new File("/sdcard/" + StaffLogin.patientDetails.getImageName());
        if (file.exists()) {
            if (file.delete())
                System.out.println(file.getAbsolutePath() + " deleted");
        }
        StaffLogin.destroyPatient();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "PatientManager Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.ahmad.patientmanagement/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PatientManager Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.ahmad.patientmanagement/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------
    private class DownloadFile extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];  //getfile php link --> dont need it, new filename received from the server changed later
            String fileName = strings[1]; //Path and name to be saved on the tablet with extension
            String toSend = strings[2];  //json object with tagID and type of file needed (encrypted)

            String recvdFileName = getData(fileUrl,toSend); //receive file name as a string from server which is encrypted

            fileUrl = "http://"+StaffLogin.serverAdd+"/"+recvdFileName;

            String encryptFileName = "/sdcard/encrypt.enc"; //We are creating an encrypted file temporarily
            File encFile = new File(encryptFileName);

            try {
                encFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadFile(fileUrl, encFile); //download the contents of the file on the server and put it in the temp file
            try {
                AES.decryptFile(encryptFileName,fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            File file = new File(fileName);
            String appType = isPDF ? "application/pdf" : "image/*";
            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, appType);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SessionThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            for(int i = 0; i<10;i++)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(10-(i+1));


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            destroy();
            super.onPostExecute(aVoid);
        }
    }





    public String getData(String link,String jsonObject)
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
            String post_data = URLEncoder.encode("client_response", "UTF-8") + "=" + URLEncoder.encode(jsonObject, "UTF-8");
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

    private static final int MEGABYTE = 1024*1024;

    public void downloadFile(String fileUrl, File directory){
        try{
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            String newBuffer = "";
            while((bufferLength = inputStream.read(buffer)) > 0){
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
    }

}
