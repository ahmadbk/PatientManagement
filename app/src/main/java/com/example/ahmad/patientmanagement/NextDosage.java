package com.example.ahmad.patientmanagement;

/**
 * Created by Ahmad on 9/7/2016.
 */
public class NextDosage {

    String medName, quantity, mealRelation, prescription_id;

    public NextDosage(String mN, String q, String mR, String pID){

        this.setMealRelation(mR);
        this.setMedName(mN);
        this.setQuantity(q);
        this.setPrescription_id(pID);

    }

    public String getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(String prescription_id) {
        this.prescription_id = prescription_id;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMealRelation() {
        return mealRelation;
    }

    public void setMealRelation(String mealRelation) {
        this.mealRelation = mealRelation;
    }

    public String[] getArray(){
        String[] array = new String[3];
        array[0] = medName;
        array[1] = quantity;
        array[2] = mealRelation;
        return array;
    }
}
