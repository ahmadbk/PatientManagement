package com.example.ahmad.patientmanagement;

/**
 * Created by Dawood on 2016/08/17.
 */
public class StaffDetails {

    int tag;
    String role,first_name,last_name, ward_name;

    StaffDetails(int t, String r,String f,String l,String w)
    {
        this.setTag(t);
        this.setRole(r);
        this.setFirst_name(f);
        this.setLast_name(l);
        this.setWard_name(w);
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }
}
