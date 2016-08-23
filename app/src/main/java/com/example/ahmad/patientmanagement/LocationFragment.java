package com.example.ahmad.patientmanagement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    private String[] location = new String[2];
    public LocationFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        location = getArguments().getStringArray("location");
            TextView timeView = (TextView) view.findViewById(R.id.timeStamp);
            timeView.setText("Time Stamp : " + location[0]);
            TextView wardView = (TextView) view.findViewById(R.id.ward);
            wardView.setText("Ward Name : " + location[1]);

        return view;
    }

}
