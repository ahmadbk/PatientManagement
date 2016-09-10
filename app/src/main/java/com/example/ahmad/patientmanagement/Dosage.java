package com.example.ahmad.patientmanagement;

/**
 * Created by Ahmad on 9/10/2016.
 */
public class Dosage {

    String nurse, time, medicineName, quantity;

    Dosage(String nurse, String time, String medicineName, String quantity)
    {
        this.setQuantity(quantity);
        this.setMedicineName(medicineName);
        this.setNurse(nurse);
        this.setTime(time);
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
}
