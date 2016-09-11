package com.example.ahmad.patientmanagement;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class PatientManager extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    DatePickerFragment mDialogFragment;
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    PatientDetails patientDetails;
    private boolean tempDateSet = false;
    private String tempDate = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_manager);

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);

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

            // Create a new Fragment to be placed in the activity layout
            PatientInfo firstFragment = new PatientInfo();
           /* Bundle bundle = new Bundle();
            bundle.putString("First Name", patientDetails.getFirstname());
            bundle.putString("Last Name", patientDetails.getSurname());
            bundle.putString("Date of Birth", patientDetails.getDOB());
            bundle.putString("Date Admitted", patientDetails.getDateAdmitted());
            bundle.putString("Emergency Contact", patientDetails.getEmergencyContact());
            firstFragment.setArguments(bundle);*/


            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.patientView, firstFragment).commit();
        }
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment tabFrags = new TabFragment();
        mFragmentTransaction.replace(R.id.containerView, tabFrags).commit();

        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nav_item_inbox) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        new DownloadFile().execute("http://" + StaffLogin.serverAdd + "/" + fileName, "/sdcard/" + fileName);
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
        StaffLogin.destroyPatient();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
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

            String fileUrl = strings[0];
            String fileName = strings[1];

            File pdfFile = new File(fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileDownloader.downloadFile(fileUrl, pdfFile);
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            File file = new File(fileName);

            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
