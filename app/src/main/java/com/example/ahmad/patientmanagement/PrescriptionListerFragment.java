package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class PrescriptionListerFragment extends Fragment {
    private int mRowCount;
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
        mRowCount = StaffLogin.patientDetails.getPrescriptionsArrayList().size();
        prescriptions = new String[mRowCount][5];

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.prescriptionLinearLayout);

        for(int i = 0; i < mRowCount; i++){

            FrameLayout frame = new FrameLayout(this.getContext());
            frame.setId(View.generateViewId());
            layout.addView(frame);

            PrescriptionFragment fragment = new PrescriptionFragment();
            Bundle bundle = new Bundle();
            prescriptions[i]=StaffLogin.patientDetails.getPrescriptionsArrayList().get(i).getArray();
            bundle.putStringArray("prescriptions", prescriptions[i]);
            fragment.setArguments(bundle);
            fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.replace(frame.getId(), fragment, "fragment" + i);
            fragTransaction.commit();
        }

        if(!StaffLogin.patientStaff) {
            if (StaffLogin.isDoctor) {
                AddPrescriptionFragment fragment = new AddPrescriptionFragment();
                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.addPrescription, fragment, "fragment");
                fragTransaction.commit();
            }
        }

        return view;
    }
}
