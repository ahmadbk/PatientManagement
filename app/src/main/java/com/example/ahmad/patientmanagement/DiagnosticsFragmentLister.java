package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class DiagnosticsFragmentLister extends Fragment {

    private int mRowCount = 1;
    private String[][] diagnostics;
    private FragmentTransaction fragTransaction;

    public DiagnosticsFragmentLister() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnostics_list, container, false);
        mRowCount = StaffLogin.patientDetails.getDiagnosticsArrayList().size();
        diagnostics = new String[mRowCount][3];

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.diagnosticLinearLayout);

        for(int i = 0; i < mRowCount; i++){
            FrameLayout frame;
            frame = new FrameLayout(this.getContext());
            frame.setId(View.generateViewId());
            layout.addView(frame);
            DiagnosticFragment fragment = new DiagnosticFragment();
            Bundle bundle = new Bundle();
            diagnostics[i] = StaffLogin.patientDetails.getDiagnosticsArrayList().get(i).getArray();
            bundle.putStringArray("diagnostics", diagnostics[i]);
            fragment.setArguments(bundle);
            fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.replace(frame.getId(), fragment, "fragment" + i);
            fragTransaction.commit();
        }
        if(!StaffLogin.patientStaff) {
            if (StaffLogin.isDoctor) {
                AddDiagnosticFragment fragment = new AddDiagnosticFragment();
                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.addDiagnostic, fragment, "fragment");
                fragTransaction.commit();
            }
        }

        return view;
    }

}
