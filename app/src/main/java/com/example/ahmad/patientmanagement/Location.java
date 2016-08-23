package com.example.ahmad.patientmanagement;

/**
 * Created by Ahmad on 2016/08/23.
 */
public class Location {

    String time_stamp,ward_name;

    public Location(String tS,String wN){
        this.setWard_name(wN);
        this.setTime_stamp(tS);
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String[] getArray(){
        String[] array = {time_stamp, ward_name};
        return array;
    }
}
