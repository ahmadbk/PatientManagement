package com.example.ahmad.patientmanagement;

import java.util.ArrayList;

/**
 * Created by Ahmad on 8/14/2016.
 */
public class PatientDetails {

    boolean isStaff = false;
    private int tagID;
    private String firstname, surname, DOB, dateAdmitted, emergencyContact,gender,phone_num,address, curr_ward_name, smoker, alcoholic;
    private ArrayList<Diagnostics> diagnosticsArrayList = new ArrayList<Diagnostics>();
    private ArrayList<Observations> observationsArrayList = new ArrayList<Observations>();
    private ArrayList<Prescriptions> prescriptionsArrayList = new ArrayList<Prescriptions>();
    private ArrayList<Allergies> allergiesArrayList = new ArrayList<Allergies>();
    private ArrayList<Location> locationArrayList = new ArrayList<Location>();
    private ArrayList<NextDosage> nextDosageArrayList = new ArrayList<NextDosage>();
    private ArrayList<Report> reportArrayList = new ArrayList<Report>();
    private ArrayList<Dosage> dosageArrayList = new ArrayList<Dosage>();

    public PatientDetails(){}
    public PatientDetails(int tID, String fN, String sN, String Dob, String dA, String eC, String g, String pN, String a, String cW, String smoker,String alcoholic, boolean is)
    {
        this.setTagID(tID);
        this.setFirstname(fN);
        this.setSurname(sN);
        this.setDOB(Dob);
        this.setDateAdmitted(dA);
        this.setEmergencyContact(eC);
        this.setGender(g);
        this.setPhone_num(pN);
        this.setAddress(a);
        this.setCurr_ward_name(cW);
        this.setSmoker(smoker);
        this.setAlcoholic(alcoholic);
        this.setStaff(is);
    }

    public ArrayList<Dosage> getDosageArrayList() {
        return dosageArrayList;
    }

    public void setDosageArrayList(ArrayList<Dosage> dosageArrayList) {
        this.dosageArrayList = dosageArrayList;
    }

    public ArrayList<Report> getReportArrayList() {
        return reportArrayList;
    }

    public void setReportArrayList(ArrayList<Report> reportArrayList) {
        this.reportArrayList = reportArrayList;
    }

    public ArrayList<NextDosage> getNextDosageArrayList() {
        return nextDosageArrayList;
    }

    public void setNextDosageArrayList(ArrayList<NextDosage> nextDosageArrayList) {
        this.nextDosageArrayList = nextDosageArrayList;
    }

    public void addToDosageArrayList(Dosage dosage)
    {
        dosageArrayList.add(0,dosage);
    }

    public void addToAllergyArrayList(Allergies allergy){
        allergiesArrayList.add(0,allergy);
    }

    public void addToDiagnosticsArrayList(Diagnostics diagnostics){
        diagnosticsArrayList.add(0,diagnostics);
    }

    public void addToObservationsArrayList(Observations observations){
        observationsArrayList.add(0,observations);
    }

    public void addToPrescriptionsArrayList(Prescriptions prescriptions){
        prescriptionsArrayList.add(0,prescriptions);
    }

    public void addToNextDosageArrayList(NextDosage nextDosage){
        nextDosageArrayList.add(0,nextDosage);
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }

    public String getSmoker() {
        return smoker;
    }

    public void setSmoker(String smoker) {
        this.smoker = smoker;
    }

    public String getAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(String alcoholic) {
        this.alcoholic = alcoholic;
    }

    public ArrayList<Allergies> getAllergiesArrayList() {
        return allergiesArrayList;
    }

    public void setAllergiesArrayList(ArrayList<Allergies> allergiesArrayList) {
        this.allergiesArrayList = allergiesArrayList;
    }

    public ArrayList<Observations> getObservationsArrayList() {
        return observationsArrayList;
    }

    public void setObservationsArrayList(ArrayList<Observations> observationsArrayList) {
        this.observationsArrayList = observationsArrayList;
    }

    public ArrayList<Prescriptions> getPrescriptionsArrayList() {
        return prescriptionsArrayList;
    }

    public void setPrescriptionsArrayList(ArrayList<Prescriptions> prescriptionsArrayList) {
        this.prescriptionsArrayList = prescriptionsArrayList;
    }

    public ArrayList<Diagnostics> getDiagnosticsArrayList() {
        return diagnosticsArrayList;
    }

    public void setDiagnosticsArrayList(ArrayList<Diagnostics> diagnosticsArrayList) {
        this.diagnosticsArrayList = diagnosticsArrayList;
    }

    public String getCurr_ward_name() {
        return curr_ward_name;
    }

    public void setCurr_ward_name(String curr_ward_name) {
        this.curr_ward_name = curr_ward_name;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public String getAddress() {
        return address;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public int getTagID() {
        return tagID;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setDateAdmitted(String dateAdmitted) {
        this.dateAdmitted = dateAdmitted;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getFirstname() {

        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getDOB() {
        return DOB;
    }

    public String getDateAdmitted() {
        return dateAdmitted;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public ArrayList<Location> getLocationArrayList() {
        return locationArrayList;
    }

    public void setLocationArrayList(ArrayList<Location> locationArrayList) {
        this.locationArrayList = locationArrayList;
    }
}
