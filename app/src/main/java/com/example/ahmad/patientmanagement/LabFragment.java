package com.example.ahmad.patientmanagement;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class LabFragment extends Fragment {


    public LabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lab, container, false);

        Spinner spinner = (Spinner)view.findViewById(R.id.fileSpinner);
        int size = StaffLogin.patientDetails.getReportArrayList().size();
        String[] files = new String[size];
        for(int i = 0; i < size; i++)
            files[i] = StaffLogin.patientDetails.getReportArrayList().get(i).getType();

        ArrayAdapter adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.listview, files);
        spinner.setAdapter(adapter);

        return view;
    }


}
