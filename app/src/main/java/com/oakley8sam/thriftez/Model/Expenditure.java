package com.oakley8sam.thriftez.Model;

import android.util.Log;

import java.util.Locale;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by oakle on 10/24/2017.
 */

public class Expenditure extends RealmObject {
    //instance variables, including name, max and curr balances, and a realmlist of expenditures
    String note;
    int day, year, month;
    float amtSpent;

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

    //TODO: CLEAN UP
    @Override
    public String toString(){
        Log.d("run toString", "Running toString");
        String expenditureInfo = "";
        expenditureInfo += String.format(Locale.US,"%f %d/%d/%d %s \n", getAmtSpent(), getDay(), getMonth(), getYear(), getNote());
        return expenditureInfo;
    }
}