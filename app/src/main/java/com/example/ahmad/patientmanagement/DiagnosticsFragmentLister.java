package com.example.ahmad.patientmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

/*        //temporary population of array
        diagnostics[0][0] = "a";
        diagnostics[0][1] = "b";
        diagnostics[0][2] = "c";
        diagnostics[1][0] = "d";
        diagnostics[1][1] = "e";
        diagnostics[1][2] = "f";
        diagnostics[2][0] = "g";
        diagnostics[2][1] = "h";
        diagnostics[2][2] = "i";*/


        for(int i = 0; i < mRowCount && i < 5; i++){
            DiagnosticFragment fragment = new DiagnosticFragment();
            Bundle bundle = new Bundle();
            diagnostics[i] = StaffLogin.patientDetails.getDiagnosticsArrayList().get(i).getArray();
            bundle.putStringArray("diagnostics", diagnostics[i]);
            fragment.setArguments(bundle);
            fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.replace(getId(i), fragment, "fragment" + i);
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

    private int getId(int index) {
        switch(index){
            case 0: return R.id.diagnosticsView;
            case 1: return R.id.diagnosticsView1;
            case 2: return R.id.diagnosticsView2;
            case 3: return R.id.diagnosticsView3;
            case 4: return R.id.diagnosticsView4;
        }
        return R.id.diagnosticsView;
    }
}
