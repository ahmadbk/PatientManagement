package com.example.ahmad.patientmanagement;

/**
 * Created by Ahmad on 2016/08/06.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 6 ;
    boolean patientStaff = false;
    boolean isDoctor = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.tablayout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new MedicalBackground();
                case 1 : return new DiagnosticsFragmentLister();
                case 2 :
                    if(StaffLogin.patientStaff) return new LabFragment();
                    else if(StaffLogin.isDoctor) return new PrescriptionListerFragment();
                    else return new NextDosageListerFragment();
                case 3 :
                    if(StaffLogin.isDoctor) return new NextDosageListerFragment();
                    else return new ObservationsListerFragment();
                case 4 : if(StaffLogin.isDoctor) return new ObservationsListerFragment();
                    else return new LabFragment();
                case 5: return new LabFragment();
                case 6: return new LocationListerFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            if(StaffLogin.patientStaff)
                return 3;
            else if(StaffLogin.isDoctor)
                return 7;
            else
                return 5;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Medical Background";
                case 1 :
                    return "Diagnostics";
                case 2 :
                    if(StaffLogin.patientStaff)
                        return "Lab";
                    else if(StaffLogin.isDoctor)
                        return "Prescription";
                    else
                    return "Dosage";
                case 3 :
                    if(StaffLogin.isDoctor)
                        return "Dosage";
                    else
                    return "Observations";
                case 4 :
                    if(StaffLogin.isDoctor)
                        return "Observation";
                    else
                    return "Lab";
                case 5:
                    return "Lab";
                case 6:
                    return "Location";
            }
            return null;
        }
    }

}