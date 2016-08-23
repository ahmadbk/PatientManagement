package com.example.ahmad.patientmanagement;

/**
 * Created by Ahmad on 8/17/2016.
 */
public class Allergies {

    String type;

    Allergies(String t){
        this.setType(t);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
