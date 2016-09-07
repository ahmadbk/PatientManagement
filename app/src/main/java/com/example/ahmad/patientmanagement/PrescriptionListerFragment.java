package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PrescriptionListerFragment extends Fragment {
    private int mRowCount = 1;
    private String[][] prescriptions;
    private FragmentTransaction fragTransaction;

    public PrescriptionListerFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prescription_lister, container, false);
        mRowCount = getArguments().getInt("rowCount");
        prescriptions = new String[mRowCount][5];

       /* //temporary population of array
        prescriptions[0][0] = "a";
        prescriptions[0][1] = "b";
        prescriptions[0][2] = "2";
        prescriptions[0][3] = "3";
        prescriptions[0][4] = "e";
        prescriptions[1][0] = "f";
        prescriptions[1][1] = "g";
        prescriptions[1][2] = "1";
        prescriptions[1][3] = "1";
        prescriptions[1][4] = "j";
        prescriptions[2][0] = "k";
        prescriptions[2][1] = "l";
        prescriptions[2][2] = "3";
        prescriptions[2][3] = "2";
        prescriptions[2][4] = "o";*/


        for(int i = 0; i < mRowCount; i++){
            PrescriptionFragment fragment = new PrescriptionFragment();
            Bundle bundle = new Bundle();
            prescriptions[i]=StaffLogin.patientDetails.getPrescriptionsArrayList().get(i).getArray();
            bundle.putStringArray("prescriptions", prescriptions[i]);
            fragment.setArguments(bundle);
            fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.replace(getId(i), fragment, "fragment" + i);
            fragTransaction.commit();
        }

        if(!StaffLogin.patientDetails.isStaff()) {
            if(StaffLogin.staffDetails.getRole().equalsIgnoreCase("doctor")){
                AddPrescriptionFragment fragment = new AddPrescriptionFragment();
                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.addPrescription, fragment, "fragment");
                fragTransaction.commit();
            }
        }

        return view;
    }

    private int getId(int index) {
        switch(index){
            case 0: return R.id.prescriptionsView;
            case 1: return R.id.prescriptionsView1;
            case 2: return R.id.prescriptionsView2;
            case 3: return R.id.prescriptionsView3;
            case 4: return R.id.prescriptionsView4;
        }
        return R.id.prescriptionsView;
    }
}
