package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
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
            doctorView.setText(prescriptions[0]);

            TextView medView = (TextView) view.findViewById(R.id.medicine);
            medView.setText(prescriptions[1]);

            TextView quanView = (TextView) view.findViewById(R.id.quantity);
            quanView.setText(prescriptions[2]);

            TextView statusView = (TextView) view.findViewById(R.id.status);
            statusView.setText(prescriptions[3]);

            ImageView morningCheck = (ImageView) view.findViewById(R.id.morningCheckBox);
            morningCheck.setImageResource(prescriptions[4].equals("true") ? R.drawable.tick : R.drawable.cross);

            ImageView noonCheck = (ImageView)view.findViewById(R.id.noonCheckBox);
            noonCheck.setImageResource(prescriptions[5].equals("true") ? R.drawable.tick : R.drawable.cross);

            ImageView eveningCheck = (ImageView)view.findViewById(R.id.eveningCheckBox);
            eveningCheck.setImageResource(prescriptions[6].equals("true") ? R.drawable.tick : R.drawable.cross);

            TextView mealRelation = (TextView)view.findViewById(R.id.mealRelationTextView);
            String temp = prescriptions[7].equals("BeforeMeal") ? "Before" : "After";
            mealRelation.setText(temp+" Meal");
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
