package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_manager);

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

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
        mFragmentTransaction.replace(R.id.containerView,tabFrags).commit();

        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();





                if (menuItem.getItemId() == R.id.nav_item_inbox) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }

    public void setDate(View v){
        mDialogFragment = new DatePickerFragment();
        mDialogFragment.show(getSupportFragmentManager(), "timePicker");
    }

    String insertionType = "";
    public void addAllergyButtonClick(View view){
        EditText editText = (EditText)findViewById(R.id.allergyEditBox);
        insertionType = "AddAllergy";
        String allergy = editText.getText().toString();

        InsertBackgroundWorker insertBackgroundWorker = new InsertBackgroundWorker(this);
        insertBackgroundWorker.execute(insertionType, allergy);

    }

    public void addDiagnosticButtonClick(View view){
        EditText notesEdit = (EditText)findViewById(R.id.notesEditText);
        String notes = notesEdit.getText().toString();


        InsertBackgroundWorker insertBackgroundWorker = new InsertBackgroundWorker(this);
        insertBackgroundWorker.execute("AddDiagnostic", notes);
    }

    public void addPrescriptionButtonClick(View view){
        Spinner medsSpinner = (Spinner)findViewById(R.id.spinner);
        String medicine = medsSpinner.getSelectedItem().toString();

        EditText quanEdit = (EditText)findViewById(R.id.dosageQuantity);
        String quantity = quanEdit.getText().toString();

        EditText freqEdit = (EditText)findViewById(R.id.dosageFrequency);
        String frequency = freqEdit.getText().toString();

        if(!tempDateSet){
            Toast.makeText(getBaseContext(), "Enter date", Toast.LENGTH_SHORT).show();
            return;
        }

        InsertBackgroundWorker insertBackgroundWorker = new InsertBackgroundWorker(this);
        insertBackgroundWorker.execute("AddPrescription", medicine, quantity,frequency, tempDate);
        tempDateSet = false;
    }

    public void addObservationButtonCLick(View view){
        String bpn = ((SeekBar)findViewById(R.id.seekBar1)).getProgress()+"";
        String bpd = ((SeekBar)findViewById(R.id.seekBar2)).getProgress()+"";
        String temp = ((SeekBar)findViewById(R.id.seekBar3)).getProgress()+"";
        String pulse = ((SeekBar)findViewById(R.id.seekBar4)).getProgress()+"";
        String weight = ((SeekBar)findViewById(R.id.seekBar5)).getProgress()+"";

        InsertBackgroundWorker insertBackgroundWorker = new InsertBackgroundWorker(this);
        insertBackgroundWorker.execute("AddObservation", bpn, bpd,temp, pulse, weight);
    }
    public void setTempDate(String date){
        tempDate = date;
        tempDateSet = true;
    }

}
