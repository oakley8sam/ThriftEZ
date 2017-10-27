package com.oakley8sam.thriftez.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by oakle on 10/17/2017.
 */

public class BudgetCategory extends RealmObject{
    //instance variables, including name, max and curr balances, and a realmlist of expenditures
    String catName;
    float maxBalance, currBalance;
    RealmList<Expenditure> expenditureList = new RealmList<Expenditure>();

    //constructors
    public BudgetCategory(){
        catName = null;
        maxBalance = 0;
        currBalance = 0;
        expenditureList = null;

    }

    public BudgetCategory(String name, float maxBal) {
        catName = name;
        maxBalance = maxBal;
        currBalance = maxBal;
        expenditureList = null;
    }

    //copy constructor
    public BudgetCategory(BudgetCategory other){
        catName = other.getName();
        maxBalance = other.getMaxBalance();
        currBalance = other.getCurrBalance();
        expenditureList = other.getExpenditureList();
    }

    //getters and setters for name, max, and curr balance
    public String getName() {
        return catName;
    }

    public void setName(String name) {
        this.catName = name;
    }

    public float getMaxBalance() {
        return maxBalance;
    }

    public void setMaxBalance(float startingBalance) {
        this.maxBalance = startingBalance;
    }

    public float getCurrBalance() {
        return currBalance;
    }

    public void setCurrBalance(float currBalance) {
        this.currBalance = currBalance;
    }

    public void setExpenditureList(RealmList<Expenditure> otherList){
        expenditureList = otherList;
    }

    public RealmList<Expenditure> getExpenditureList(){
        return expenditureList;
    }

    public void addExpenditure(Expenditure exp){
        currBalance -= exp.getAmtSpent();
        expenditureList.add(exp);
    }
}
