package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ahmad on 2016/08/13.
 */
public class DiagnosticFragment extends Fragment {

    private String[] diagnostics = new String[3];//string array with [0]Date, [1]Doctor, [2]Notes
    public DiagnosticFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnostic, container, false);
        diagnostics = getArguments().getStringArray("diagnostics");
    try {
        TextView dateView = (TextView) view.findViewById(R.id.date);
        dateView.setText("Date: " + diagnostics[0]);
        TextView doctorView = (TextView) view.findViewById(R.id.doctor);
        doctorView.setText("Doctor: " + diagnostics[1]);

        TextView notesView = (TextView) view.findViewById(R.id.notes);
        notesView.setText("Notes: " + diagnostics[2]);
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

    private void handleArguments(Bundle arguments){}

    private void handleSavedInstanceState(Bundle savedInstanceState){}

    private void handleExtras(Bundle extras){}
}
