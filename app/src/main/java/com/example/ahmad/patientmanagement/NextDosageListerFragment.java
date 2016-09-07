package com.example.ahmad.patientmanagement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NextDosageListerFragment extends Fragment {

    private int mRowCount = 1;
    private String[][] nextDosages;
    private FragmentTransaction fragTransaction;

    public NextDosageListerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_next_dosage_lister, container, false);
        mRowCount = StaffLogin.patientDetails.getNextDosageArrayList().size();
        System.out.println(mRowCount);
        nextDosages= new String[mRowCount][3];

        for(int i = 0; i < mRowCount; i++){
            NextDosageFragment fragment = new NextDosageFragment();
            Bundle bundle = new Bundle();
            nextDosages[i] = StaffLogin.patientDetails.getNextDosageArrayList().get(i).getArray();
            bundle.putStringArray("dosage", nextDosages[i]);
            fragment.setArguments(bundle);
            fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.replace(getId(i), fragment, "fragment" + i);
            fragTransaction.commit();
        }
        return view;
    }

    private int getId(int index) {
        switch(index){
            case 0: return R.id.nextDosageView;
            case 1: return R.id.nextDosageView1;
            case 2: return R.id.nextDosageView2;
            case 3: return R.id.nextDosageView3;
            case 4: return R.id.nextDosageView4;
        }
        return R.id.nextDosageView;
    }
}
