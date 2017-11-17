package com.oakley8sam.thriftez.Model;


import io.realm.RealmObject;


public class Expenditure extends RealmObject {
    //instance variables, including name, max and curr balances, and a realmlist of expenditures
    String note;
    int day, year, month;
    float amtSpent;

    //expenditure constructors
    public Expenditure(){
        note = "";
        month = 1;
        day = 1;
        year = 1;
        amtSpent = 0;

    }

    public Expenditure (String no, int mon, int da, int yr, float amt){
        note = no;
        month = mon;
        day = da;
        year = yr;
        amtSpent = amt;
    }

    //getters and setters
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getAmtSpent() {
        return amtSpent;
    }

    public void setAmtSpent(float amtSpent) {
        this.amtSpent = amtSpent;
    }

}