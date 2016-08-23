package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ObservationFragment extends Fragment {
    private String[] observations = new String[7];
    public ObservationFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observation, container, false);
        observations = getArguments().getStringArray("observations");
        try {

            TextView dateView = (TextView)view.findViewById(R.id.obsDate);
            dateView.setText("Date : " + observations[0]);

            TextView doctorView = (TextView) view.findViewById(R.id.doctor);
            doctorView.setText("Doctor : " + observations[1]);

            TextView pressureView = (TextView) view.findViewById(R.id.pressure);
            pressureView.setText("Blood Pressure : " + observations[2]+"/"+observations[3]);

            TextView tempView = (TextView) view.findViewById(R.id.tempereture);
            tempView.setText("Tempereture : " + observations[4] + (char) 0x00B0 + "C");

            TextView pulseView = (TextView) view.findViewById(R.id.pulse);
            pulseView.setText("Pulse : " + observations[5] + " BPM");

            TextView weightView = (TextView)view.findViewById(R.id.weight);
            weightView.setText("Weight: " + observations[6] + "kg");
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
