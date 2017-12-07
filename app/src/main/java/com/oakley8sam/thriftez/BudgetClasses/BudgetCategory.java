package com.oakley8sam.thriftez.BudgetClasses;

import io.realm.RealmList;
import io.realm.RealmObject;


public class BudgetCategory extends RealmObject{
    //instance variables, including name, max and curr balances, and a realmlist of expenditures
    String catName;
    float maxBalance, currBalance, spent, reimbursed;
    RealmList<Expenditure> expenditureList = new RealmList<Expenditure>();
    RealmList<Expenditure> repaymentList = new RealmList<Expenditure>();


    //constructors
    public BudgetCategory(){
        catName = null;
        maxBalance = 0;
        currBalance = 0;
        spent = 0;
        reimbursed = 0;
    }

    public BudgetCategory(String name, float maxBal) {
        catName = name;
        maxBalance = maxBal;
        currBalance = maxBal;
    }

    //copy constructor
    public BudgetCategory(BudgetCategory other){
        catName = other.getName();
        maxBalance = other.getMaxBalance();
        currBalance = other.getCurrBalance();
        spent = other.getSpent();
        reimbursed = other.getReimbursed();
        expenditureList = other.getExpenditureList();
        repaymentList = other.getRepaymentList();
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

    public float getSpent() {return spent;}

    public void setSpent(float newSpent) {spent = newSpent;}

    public float getReimbursed() {return reimbursed;}

    public void setReimbursed(float newReimbursed) {reimbursed = newReimbursed;}

    public void setExpenditureList(RealmList<Expenditure> otherList){
        expenditureList = otherList;
    }

    public RealmList<Expenditure> getExpenditureList(){
        return expenditureList;
    }

    public void setRepaymentList(RealmList<Expenditure> otherList){
        repaymentList = otherList;
    }

    public RealmList<Expenditure> getRepaymentList() { return repaymentList; }

    //adds the expenditure to the category, updating appropriate values
    public void addExpenditure(Expenditure exp) {
        currBalance -= exp.getAmtSpent();
        spent += exp.getAmtSpent();
        if (expenditureList.size() == 0) {
            expenditureList.add(exp);
        } else {
            for (int i = 0; i < expenditureList.size(); i++) {
                if (expenditureList.get(i).getDay() >= exp.getDay()) {
                    expenditureList.add(i, exp);
                    return;
                }
            }
            expenditureList.add(exp);
        }
    }

    //adds a repayment to the category, updating appropriate values
    public void addRepayment(Expenditure reimbursal) {
        currBalance +=reimbursal.getAmtSpent();
        reimbursed += reimbursal.getAmtSpent();
        if(repaymentList.size() == 0){
            repaymentList.add(reimbursal);
        } else {
            for (int i = 0; i < repaymentList.size(); i++){
                if (repaymentList.get(i).getDay() >= reimbursal.getDay()){
                    repaymentList.add(i,reimbursal);
                    return;
                }
            }
            repaymentList.add(reimbursal);
        }
    }
}
