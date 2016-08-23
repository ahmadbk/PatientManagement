package com.example.ahmad.patientmanagement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class AddObservationFragment extends Fragment {
    private View view;
    // private TextView dateView;

    public AddObservationFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_observation, container, false);

        //dateView = (TextView)view.findViewById(R.id.obsDate);
        final TextView view1 = (TextView)view.findViewById(R.id.bpn);
        SeekBar seekBar1 = (SeekBar)view.findViewById(R.id.seekBar1);
        view1.setText(seekBar1.getProgress()+"");
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                view1.setText(i+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView view2 = (TextView)view.findViewById(R.id.bpd);
        SeekBar seekBar2 = (SeekBar)view.findViewById(R.id.seekBar2);
        view2.setText(seekBar2.getProgress()+"");
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                view2.setText(i+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView view3 = (TextView)view.findViewById(R.id.temp);
        SeekBar seekBar3 = (SeekBar)view.findViewById(R.id.seekBar3);
        view3.setText(seekBar3.getProgress()+""+(char) 0x00B0 + "C");
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                view3.setText(i+" "+(char) 0x00B0 + "C");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView view4 = (TextView)view.findViewById(R.id.pulse);
        SeekBar seekBar4 = (SeekBar)view.findViewById(R.id.seekBar4);
        view4.setText(seekBar4.getProgress()+" BPM");
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                view4.setText(i+" BPM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView view5 = (TextView)view.findViewById(R.id.weight);
        SeekBar seekBar5 = (SeekBar)view.findViewById(R.id.seekBar5);
        view5.setText(seekBar5.getProgress()+" kg");
        seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                view5.setText(i+" kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    public void setDate(String s){
        TextView dateView = (TextView)view.findViewById(R.id.obsDate);
        dateView.setText("Date : "+s);
    }

}
