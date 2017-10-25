package com.oakley8sam.thriftez.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by oakle on 10/24/2017.
 */

public class Expenditure extends RealmObject {
    //instance variables, including name, max and curr balances, and a realmlist of expenditures
    String note, month;
    int day, year;
    float amtSpent;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
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

    @Override
    public String toString(){
        String expenditureInfo = "";
        return expenditureInfo;
    }
}