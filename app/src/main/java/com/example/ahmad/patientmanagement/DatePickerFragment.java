package com.example.ahmad.patientmanagement;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Dawood on 2016/08/14.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private AddPrescriptionFragment mPrescriptionFragment;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        month++;
        String m = month+"";
        if(month < 10)
            m = "0"+m;

        String d = day+"";
        if(day < 9)
            d = "0"+d;
        String date = ""+year+"-"+m+"-"+d;
        PatientManager activity = (PatientManager)getActivity();
        mPrescriptionFragment= (AddPrescriptionFragment)activity.getSupportFragmentManager().
                findFragmentById(R.id.containerView).getChildFragmentManager().findFragmentById(R.id.addPrescription);
        mPrescriptionFragment.setDate(date);
        activity.setTempDate(date);
    }

}