package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MedicalBackground extends Fragment {

    private String[] StringArray;
    private FragmentTransaction fragTransaction;
    public MedicalBackground() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ;
    }

    @Override
    public void onResume(){
        super.onResume();
        int size = StaffLogin.patientDetails.getAllergiesArrayList().size();
        StringArray = new String[size];
        for(int i = 0; i < size; i++)
            StringArray[i] = StaffLogin.patientDetails.getAllergiesArrayList().get(i).getType();
        ListView listView = (ListView)getView().findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.listview, StringArray);
        listView.setAdapter(adapter);

        ImageView smokerView = (ImageView) getView().findViewById(R.id.smokerId);
        ImageView alcoholicView = (ImageView) getView().findViewById((R.id.alcoholicId));

        smokerView.setImageResource(StaffLogin.patientDetails.getSmoker().equalsIgnoreCase("yes") ? R.drawable.tick : R.drawable.cross);
        alcoholicView.setImageResource(StaffLogin.patientDetails.getAlcoholic().equalsIgnoreCase("yes") ? R.drawable.tick : R.drawable.cross);

        if(!StaffLogin.patientStaff) {
            if (StaffLogin.isDoctor) {
                AddAllergyFragment fragment = new AddAllergyFragment();
                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.addAllergy, fragment, "fragment");
                fragTransaction.commit();
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_medical_background, container, false);
        return view;
    }
}
