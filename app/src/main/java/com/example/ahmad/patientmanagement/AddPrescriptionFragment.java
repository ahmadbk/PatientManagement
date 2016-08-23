package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class AddPrescriptionFragment extends Fragment {

    public AddPrescriptionFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_prescription, container, false);
        Spinner spinner = (Spinner)view.findViewById(R.id.spinner);
        String[] tempArray = BackgroundWorker.medArray;
        int count = 0;
        for(;tempArray[count]!=null;count++ ){}
        String[] medicines = new String[count];
        System.arraycopy(tempArray, 0, medicines, 0, count);

        ArrayAdapter adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.listview, medicines);
        spinner.setAdapter(adapter);

        return view;
    }

    public void setDate(String date){
        TextView dateView = (TextView)getView().findViewById(R.id.endDatePrescription);
        dateView.setText("End Date : "+date);
    }
}
