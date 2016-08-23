package com.example.ahmad.patientmanagement;

/**
 * Created by Ahmad on 2016/08/15.
 */
public class Diagnostics {

    private String date, doctor_name, description;

    Diagnostics(String date, String doctor, String descrip)
    {
        this.setDate(date);
        this.setDoctor_name(doctor);
        this.setDescription(descrip);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getArray(){
        String[] array = new String[3];
        array[0] = date;
        array[1] = doctor_name;
        array[2] = description;
        return array;
    }
}
