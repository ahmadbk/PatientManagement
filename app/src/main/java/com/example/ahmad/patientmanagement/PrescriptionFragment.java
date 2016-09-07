package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


public class PrescriptionFragment extends Fragment {
    private String[] prescriptions = new String[5];
    public PrescriptionFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prescription, container, false);
        prescriptions = getArguments().getStringArray("prescriptions");
        try {
            TextView doctorView = (TextView) view.findViewById(R.id.doctor);
            doctorView.setText("Doctor : " + prescriptions[0]);

            TextView medView = (TextView) view.findViewById(R.id.medicine);
            medView.setText("Med Name : " + prescriptions[1]);

            TextView quanView = (TextView) view.findViewById(R.id.quantity);
            quanView.setText("Quantity : " + prescriptions[2]);

            TextView statusView = (TextView) view.findViewById(R.id.status);
            statusView.setText("Status : " + prescriptions[3]);

            CheckBox morningCheck = (CheckBox)view.findViewById(R.id.morningCheckBox);
            morningCheck.setChecked(prescriptions[4].equals("true") ? true : false);

            CheckBox noonCheck = (CheckBox)view.findViewById(R.id.noonCheckBox);
            noonCheck.setChecked(prescriptions[5].equals("true") ? true : false);

            CheckBox eveningCheck = (CheckBox)view.findViewById(R.id.eveningCheckBox);
            eveningCheck.setChecked(prescriptions[6].equals("true") ? true : false);

            TextView mealRelation = (TextView)view.findViewById(R.id.mealRelationTextView);
            String temp = prescriptions[7].equals("BeforeMeal") ? "before" : "after";
            mealRelation.setText("Medicine to be taken "+temp+" meals");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

}
