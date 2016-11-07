package com.example.ahmad.patientmanagement;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DosageFragment extends Fragment {

    private ArrayList<Dosage> tempArr = new ArrayList<Dosage>();

    public DosageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dosage, container, false);
        int size = StaffLogin.patientDetails.getDosageArrayList().size();
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.dosageLinearLayout);
        boolean[] flags = new boolean[size];
        for(boolean b: flags)b = false;

        for(int i = 0; i < size; i++){
            System.out.println(StaffLogin.patientDetails.getDosageArrayList().get(i).getMedicineName() +
             " : " + StaffLogin.patientDetails.getDosageArrayList().get(i).getTime());
        }

        int counter = 0;
        int groupCount = 0;
        while(counter < size){
            if(!flags[counter]) {
                String timeStamp = timeStampToDate(StaffLogin.patientDetails.getDosageArrayList().get(counter).getTime());

                for (int i = counter + 1; i < size; i++) {//group dates together
                    if (!flags[i])
                        if (timeStamp.equals(timeStampToDate(StaffLogin.patientDetails.getDosageArrayList().get(i).getTime()))) {
                            //if dates are equal check if period is equal
                            if (StaffLogin.patientDetails.getDosageArrayList().get(counter).getPeriod().equals(
                                    StaffLogin.patientDetails.getDosageArrayList().get(i).getPeriod())) {
                                //if period is also equal add to temp arraylist
                                tempArr.add(StaffLogin.patientDetails.getDosageArrayList().get(i));
                                flags[i] = true;
                            }
                        }
                }
                if (!flags[counter]) {
                    tempArr.add(0, StaffLogin.patientDetails.getDosageArrayList().get(counter));
                    flags[counter] = true;
                    groupCount++;
                    //if(groupCount > 5)return view;
                }

                LinearLayout frame = new LinearLayout(getContext());
                frame.setOrientation(LinearLayout.VERTICAL);
                frame.setBackgroundResource(R.drawable.border);
                frame.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TextView textView = new TextView(this.getContext());//(TextView)view.findViewById(getTextViewId(groupCount-1));
                textView.setId(View.generateViewId());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,8,8,8);

                textView.setLayoutParams(params);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setText(timeStamp + ": " +
                        StaffLogin.patientDetails.getDosageArrayList().get(counter).getPeriod());

                System.out.println("Group-------------------"+groupCount);//Each group here
                int s = tempArr.size();
               // System.out.println((groupCount-1)+" id");
                String[] StringArray = new String[s];
                for(int i = 0; i < s; i++){
                    StringArray[i] = tempArr.get(i).toString();
                }
                ListView listView = new ListView(getContext());//(ListView)view.findViewById(getId(groupCount-1));
                listView.setId(View.generateViewId());
                params.setMargins(16,16,16,16);
                listView.setLayoutParams(params);
                ArrayAdapter adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.listview, StringArray);
                listView.setAdapter(adapter);

                ListAdapter listAdapter = listView.getAdapter();
                int totalHeight = 0;
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    View listItem = listAdapter.getView(i, null, listView);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }

                params = (LinearLayout.LayoutParams)listView.getLayoutParams();
                params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
                listView.setLayoutParams(params);
                listView.requestLayout();

                frame.addView(textView);
                frame.addView(listView);
                layout.addView(frame);

                while (tempArr.size() > 0) {
                    System.out.println(tempArr.get(0).getMedicineName() + " : " + tempArr.get(0).getTime());
                    tempArr.remove(0);
                }
            }
            counter++;
            if(groupCount > size) break;
        }

        return view;
    }

    private String timeStampToDate(String ts){
        return ts.substring(0,11);
    }
}
