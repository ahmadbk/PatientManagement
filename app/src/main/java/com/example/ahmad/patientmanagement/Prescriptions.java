package com.example.ahmad.patientmanagement;

/**
 * Created by Dawood on 2016/08/15.
 */
public class Prescriptions {
    String start_date, end_date, doctor_name,medicine_name,quantity_per_day,frequency_per_day,status;

    Prescriptions(String sD, String eD, String dN, String mN, String q, String f, String s)
    {
        this.setStart_date(sD);
        this.setEnd_date(eD);
        this.setDoctor_name(dN);
        this.setMedicine_name(mN);
        this.setQuantity_per_day(q);
        this.setFrequency_per_day(f);
        this.setStatus(s);
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getQuantity_per_day() {
        return quantity_per_day;
    }

    public void setQuantity_per_day(String quantity_per_day) {
        this.quantity_per_day = quantity_per_day;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getFrequency_per_day() {
        return frequency_per_day;
    }

    public void setFrequency_per_day(String frequency_per_day) {
        this.frequency_per_day = frequency_per_day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getArray(){
        String[] array = new String[6];
        array[0] = doctor_name;
        array[1] = medicine_name;
        array[2] = quantity_per_day;
        array[3] = frequency_per_day;
        array[4] = status;
        return array;
    }
}
