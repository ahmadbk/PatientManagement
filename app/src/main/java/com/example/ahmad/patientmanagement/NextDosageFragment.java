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
public class NextDosageFragment extends Fragment {

    private String[] nextDosage = new String[3];

    public NextDosageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_next_dosage, container, false);
        nextDosage = getArguments().getStringArray("dosage");

        TextView medicine = (TextView)view.findViewById(R.id.nextDosageMedicineName);
        medicine.setText("Medicine : " + nextDosage[0]);

        TextView quantity = (TextView)view.findViewById(R.id.nextDosageQuantity);
        quantity.setText(nextDosage[1] + " " + (nextDosage[2].equalsIgnoreCase("beforemeal") ? "before meal" : "after meal"));
        return view;
    }

}
