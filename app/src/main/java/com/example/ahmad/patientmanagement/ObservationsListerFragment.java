package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ObservationsListerFragment extends Fragment {
    private int mRowCount = 1;
    private String[][] observations;
    private FragmentTransaction fragTransaction;

    public ObservationsListerFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observations_lister, container, false);
        mRowCount = getArguments().getInt("rowCount");
        observations = new String[mRowCount][7];

     /*   //temporary population of array
        observations[0][0] = "a";
        observations[0][1] = "b";
        observations[0][2] = "2";
        observations[0][3] = "3";
        observations[0][4] = "e";
        observations[0][5] = "e";
        observations[0][6] = "5";
        observations[1][0] = "f";
        observations[1][1] = "g";
        observations[1][2] = "1";
        observations[1][3] = "1";
        observations[1][4] = "j";
        observations[1][5] = "e";
        observations[1][6] = "10";*/


        for(int i = 0; i < mRowCount; i++){
            ObservationFragment fragment = new ObservationFragment();
            Bundle bundle = new Bundle();
            observations[i] = StaffLogin.patientDetails.getObservationsArrayList().get(i).getArray();
            bundle.putStringArray("observations", observations[i]);
            fragment.setArguments(bundle);
            fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.replace(getId(i), fragment, "fragment" + i);
            fragTransaction.commit();
        }

        if(!StaffLogin.patientDetails.isStaff()) {
            if (StaffLogin.staffDetails.getRole().equalsIgnoreCase("doctor")) {
                AddObservationFragment fragment = new AddObservationFragment();
                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.addObservation, fragment, "fragment");
                fragTransaction.commit();
            }
        }
        return view;
    }

    private int getId(int index) {
        switch(index){
            case 0: return R.id.observationsView;
            case 1: return R.id.observationsView1;
            case 2: return R.id.observationsView2;
            case 3: return R.id.observationsView3;
            case 4: return R.id.observationsView4;
        }
        return R.id.observationsView;
    }
}
