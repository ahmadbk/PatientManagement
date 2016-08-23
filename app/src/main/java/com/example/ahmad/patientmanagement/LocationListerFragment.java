package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LocationListerFragment extends Fragment {
    private int mRowCount = 1;
    private String[][] locations;
    FragmentTransaction fragTransaction;

    public LocationListerFragment() {//TODO everything
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_lister, container, false);
        mRowCount = BackgroundWorker.patientDetails.getLocationArrayList().size();
        locations = new String[mRowCount][2];

        for(int i = 0; i < mRowCount; i++){
            LocationFragment fragment = new LocationFragment();
            Bundle bundle = new Bundle();
            locations[i] = BackgroundWorker.patientDetails.getLocationArrayList().get(i).getArray();
            bundle.putStringArray("location", locations[i]);
            fragment.setArguments(bundle);
            fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.replace(getId(i), fragment, "fragment" + i);
            fragTransaction.commit();
        }

        return view;
    }

    private int getId(int index) {
        switch(index){
            case 0: return R.id.locationsView;
            case 1: return R.id.locationsView1;
            case 2: return R.id.locationsView2;
            case 3: return R.id.locationsView3;
            case 4: return R.id.locationsView4;
        }
        return R.id.locationsView;
    }
}
