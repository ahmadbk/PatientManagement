package com.example.ahmad.patientmanagement;

/**
 * Created by Ahmad on 9/9/2016.
 */
public class Report {

    String date, type, nameOfFile, link;

    Report(String d, String t, String NOF)
    {
        this.setDate(d);
        this.setNameOfFile(NOF);
        this.setType(t);
        link = "http://" + StaffLogin.serverAdd + "/" + this.getNameOfFile();
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameOfFile() {
        return nameOfFile;
    }

    public void setNameOfFile(String nameOfFile) {
        this.nameOfFile = nameOfFile;
    }
}
