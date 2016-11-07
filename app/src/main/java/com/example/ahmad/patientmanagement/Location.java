package com.example.ahmad.patientmanagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ahmad on 2016/08/23.
 */
public class Location implements Comparable<Location>{

    String time_stamp,ward_name;

    public Location(String tS,String wN){
        this.setWard_name(StaffLogin.makeHeading(wN));
        System.out.println(this.getWard_name()+".");
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

    @Override
    public String toString(){
        String s = getTime_stamp() + " " + getWard_name();
        return s;
    }

    @Override
    public int compareTo(Location location) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date myDate = (Date)formatter.parse(this.getTime_stamp());
            Date yourDate = (Date)formatter.parse(location.getTime_stamp());

            if(myDate.getYear() > yourDate.getYear())
                return -1;
            else if(myDate.getYear() < yourDate.getYear())
                return 1;
            if(myDate.getMonth() > yourDate.getMonth())
                return -1;
            else if(myDate.getMonth() < yourDate.getMonth())
                return 1;
            if(myDate.getDay() > yourDate.getDay())
                return -1;
            else if(myDate.getDay() < yourDate.getDay())
                return 1;
            if(myDate.getHours() > yourDate.getHours())
                return -1;
            else if(myDate.getHours() < yourDate.getHours())
                return 1;
            if(myDate.getMinutes() > yourDate.getMinutes())
                return -1;
            else if(myDate.getMinutes() < yourDate.getMinutes())
                return 1;
            if(myDate.getSeconds() > yourDate.getSeconds())
                return -1;
            else if(myDate.getSeconds() < yourDate.getSeconds())
                return 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
