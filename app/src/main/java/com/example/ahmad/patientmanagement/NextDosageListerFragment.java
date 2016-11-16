package com.example.ahmad.patientmanagement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


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
        System.out.println(StaffLogin.isDoctor);
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

        if(StaffLogin.isDoctor){
            Button button = (Button)view.findViewById(R.id.dealDosageButton);
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
            for(int i = 0; i < mRowCount; i++){
                CheckBox cb = (CheckBox)view.findViewById(getCheckBoxId(i));
                cb.setEnabled(false);
                cb.setVisibility(View.INVISIBLE);
            }
        }

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.nextDosageLinearLayout);

        for(int i = mRowCount; i < 5; i++){
            LinearLayout hoLayout = (LinearLayout)view.findViewById(getId2(i));
            layout.removeViewInLayout(hoLayout);
        }

        if(!StaffLogin.patientStaff) {
            if (StaffLogin.isDoctor) {
                DosageFragment fragment = new DosageFragment();
                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.dosagesFragment, fragment, "fragment");
                fragTransaction.commit();
            }
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

    public static int getCheckBoxId(int index){
        switch(index){
            case 0: return R.id.dosageCheckOne;
            case 1: return R.id.dosageCheckTwo;
            case 2: return R.id.dosageCheckThree;
            case 3: return R.id.dosageCheckFour;
            case 4: return R.id.dosageCheckFive;
        }
        return R.id.dosageCheckOne;
    }

    public static int getId2(int index){
        switch(index){
            case 0: return R.id.nextDosageHoriontal;
            case 1: return R.id.nextDosageHoriontal1;
            case 2: return R.id.nextDosageHoriontal2;
            case 3: return R.id.nextDosageHoriontal3;
            case 4: return R.id.nextDosageHoriontal4;
        }
        return R.id.dosageCheckOne;
    }
}
