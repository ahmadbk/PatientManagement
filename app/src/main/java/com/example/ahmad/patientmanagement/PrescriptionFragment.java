package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            quanView.setText("Quantity : " + prescriptions[2] + " " + prescriptions[3]);

            TextView statusView = (TextView) view.findViewById(R.id.status);
            statusView.setText("Status : " + prescriptions[4]);
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