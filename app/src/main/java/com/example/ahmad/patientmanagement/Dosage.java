package com.example.ahmad.patientmanagement;

/**
 * Created by Ahmad on 9/10/2016.
 */
public class Dosage {

    String nurse, time, medicineName, quantity,period;

    Dosage(String nurse, String time, String medicineName, String quantity,String period)
    {
        this.setQuantity(quantity);
        this.setMedicineName(StaffLogin.makeHeading(medicineName));
        this.setNurse(StaffLogin.makeHeading(nurse));
        this.setTime(time);
        this.setPeriod(StaffLogin.makeHeading(period));
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNurse() {
        return nurse;
    }

    public void setNurse(String nurse) {
        this.nurse = nurse;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString(){
        String s = medicineName + ": " + quantity;
        return s;
    }
}
