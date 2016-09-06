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
                case 0 : return new MedicalBackground();//TODO set bundle arguments
                case 1 :
                    DiagnosticsFragmentLister frag = new DiagnosticsFragmentLister();
                    Bundle bundle = new Bundle();
                    bundle.putInt("rowCount", StaffLogin.patientDetails.getDiagnosticsArrayList().size());
                    frag.setArguments(bundle);
                    return frag;
                case 2 :
                    PrescriptionListerFragment frag1 = new PrescriptionListerFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("rowCount", StaffLogin.patientDetails.getPrescriptionsArrayList().size());
                    frag1.setArguments(bundle1);
                    return frag1;
                case 3 :
                    ObservationsListerFragment frag2 = new ObservationsListerFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("rowCount", StaffLogin.patientDetails.getObservationsArrayList().size());
                    frag2.setArguments(bundle2);
                    return frag2;
                case 4 : return new OneFragment();
                case 5: return new LocationListerFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

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
                    return "Prescription";
                case 3 :
                    return "Observation";
                case 4 :
                    return "Lab";
                case 5:
                    return "Locations";
            }
            return null;
        }
    }

}