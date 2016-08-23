package com.example.ahmad.patientmanagement;

/**
 * Created by Dawood on 2016/08/15.
 */
public class Observations {

    String date, doc_name, bpn,bpd,temperature,pulse,weight;

    Observations(String d, String dN, String bpn, String bpd, String t, String p, String w)
    {
        this.setDate(d);
        this.setDoc_name(dN);
        this.setBpn(bpn);
        this.setBpd(bpd);
        this.setTemperature(t);
        this.setPulse(p);
        this.setWeight(w);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getBpn() {
        return bpn;
    }

    public void setBpn(String bpn) {
        this.bpn = bpn;
    }

    public String getBpd() {
        return bpd;
    }

    public void setBpd(String bpd) {
        this.bpd = bpd;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String[] getArray(){
        String[] array = new String[7];
        array[0] = date;
        array[1] = doc_name;
        array[2] = bpn;
        array[3] = bpd;
        array[4] = temperature;
        array[5] = pulse;
        array[6] = weight;
        return array;
    }
}
