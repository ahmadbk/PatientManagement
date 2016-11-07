package com.example.ahmad.patientmanagement;

import java.util.ArrayList;

/**
 * Created by Ahmad on 2016/08/15.
 */
public class Prescriptions {
    String start_date, end_date, doctor_name,medicine_name,quantity_per_dosage,status,morning,afternoon,evening,mealRelation;

    Prescriptions(String sD, String eD, String dN, String mN, String q, String s,String mor,String aft, String eve, String mR)
    {
        this.setStart_date(sD);
        this.setEnd_date(eD);
        this.setDoctor_name(StaffLogin.makeHeading(dN));
        this.setMedicine_name(StaffLogin.makeHeading(mN));
        this.setQuantity_per_dosage(q);
        this.setStatus(StaffLogin.makeHeading(s));
        this.setMorning(mor);
        this.setEvening(eve);
        this.setAfternoon(aft);
        this.setMealRelation(mR);
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public String getEvening() {
        return evening;
    }

    public void setEvening(String evening) {
        this.evening = evening;
    }

    public String getMealRelation() {
        return mealRelation;
    }

    public void setMealRelation(String mealRelation) {
        this.mealRelation = mealRelation;
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

    public String getQuantity_per_dosage() {
        return quantity_per_dosage;
    }

    public void setQuantity_per_dosage(String quantity_per_day) {
        this.quantity_per_dosage = quantity_per_day;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getArray(){
        String[] array = new String[8];
        array[0] = doctor_name;
        array[1] = medicine_name;
        array[2] = quantity_per_dosage;
        array[3] = status;
        array[4] = morning;
        array[5] = afternoon;
        array[6] = evening;
        array[7] = mealRelation;

        return array;
    }


}
