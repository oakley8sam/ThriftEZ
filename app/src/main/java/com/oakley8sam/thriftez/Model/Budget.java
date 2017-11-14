package com.oakley8sam.thriftez.Model;

import android.util.Log;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import java.lang.Math;
import java.util.Locale;

import io.realm.annotations.PrimaryKey;

/**
 * Created by oakle on 10/17/2017.
 */

public class Budget extends RealmObject{

    //instance variables, a list of categories and all expenditures across all categories
    //and floats for max and current funds
    RealmList<BudgetCategory> categoryList = new RealmList<BudgetCategory>();
    RealmList<Expenditure> expenditureMasterList = new RealmList<Expenditure>();
    private float totalMaxFunds, totalCurrFunds;
    private int monthCreated, yearCreated;

    //getters and setters for fund floats
    public float getTotalMaxFunds() {
        return totalMaxFunds;
    }

    public void setTotalMaxFunds(float totalMaxFunds) {
        this.totalMaxFunds = totalMaxFunds;
    }

    public float getTotalCurrFunds() {
        return totalCurrFunds;
    }

    public void setTotalCurrFunds(float totalCurrFunds) {
        this.totalCurrFunds = totalCurrFunds;
    }

    public void setMonthCreated(int newMonth){ monthCreated = newMonth; }

    public int getMonthCreated(){return monthCreated;}

    public void setYearCreated (int newYear) {yearCreated = newYear;}

    public int getYearCreated(){return yearCreated;}

    public RealmList<BudgetCategory> getCatList(){
        return categoryList;
    }

    // returns the names of the categories as a ArrayList
    public ArrayList<String> getCatListString() {
        ArrayList<String> catList = new ArrayList<String>();
        for (int i = 0; i< categoryList.size(); i++){
            catList.add(categoryList.get(i).getName());
        }
        return catList;
    }

    public RealmList<Expenditure> getExpenditureMasterList() {return expenditureMasterList;}

    //updates max and curr funds based on max and curr funds for each category
    public void updateFunds(){
        totalMaxFunds=0;
        totalCurrFunds=0;
        for (int i=0; i<categoryList.size();i++){
            totalMaxFunds+=categoryList.get(i).getMaxBalance();
            totalCurrFunds+=categoryList.get(i).getCurrBalance();
        }
    }

    //adds a category to the list, increasing max and current funds
    public void addCategory(BudgetCategory cat){
        categoryList.add(categoryList.size(),cat);
        updateFunds();
    }

    //removes a category from the list, decreasing max and current funds
    public boolean removeCategory(BudgetCategory cat){
        String nameToRemove = cat.getName();
        int listSize = categoryList.size();
        for(int i = 0; i < listSize; i++){
            if (categoryList.get(i).getName().equals(nameToRemove)) {
                categoryList.remove(i);
                updateFunds();
                return true;
            }
        }
        updateFunds();
        return false;
    }

    public float editCategory(BudgetCategory cat, float newBal){
        String nameToEdit = cat.getName();
        int listSize = categoryList.size();
        for (int i = 0; i < listSize; i++){
            BudgetCategory catToEdit = categoryList.get(i);
            if (catToEdit.getName().equals(nameToEdit)) {
                catToEdit.setMaxBalance(newBal);
                if(catToEdit.getCurrBalance() > catToEdit.getMaxBalance())
                    catToEdit.setCurrBalance(catToEdit.getMaxBalance());
            }
        }
        updateFunds();
        return newBal;
    }

    /////////////////////////////////////////////
    //adds an expenditure to the budget's master list, sorted by day of the month
    public void addExpendituretoMaster(Expenditure exp) {
        Log.d("IN MASTER ADD", "ADDING EXPENDITURE TO MASTER");
        if (expenditureMasterList.size() == 0) {
            expenditureMasterList.add(exp);
        } else {
            for (int i = 0; i < expenditureMasterList.size(); i++) {
                Log.d("expenditure master list", "Exp at Index " + i + " = " + expenditureMasterList.get(i).getNote() + "       ");
                if (expenditureMasterList.get(i).getDay() >= exp.getDay()) {
                    expenditureMasterList.add(i, exp);
                    return;
                }
            }
            expenditureMasterList.add(exp);
        }
    }
    ///////////////////////////////////////////////

    //budget copy constructor
    //duplicates the current budget's categories and their max vals, does not copy expenditures. Used to refresh the budget at the beginning of the month
    public void duplicateBudget (Budget other){
        Log.d("duplicate", "DUPLICATING BUDGET");
        this.totalMaxFunds = other.totalMaxFunds;
        Log.d("copee max", "COPEE'S MAX =" +other.totalMaxFunds);
        Log.d("dupe max", "DUPLICATE'S MAX = " + totalMaxFunds );
        this.totalCurrFunds = other.totalMaxFunds;
        Log.d("copee curr", "COPEE's CURR = " + other.totalCurrFunds);
        Log.d("dupe curr", "DUPLICATE'S CURR = " + totalCurrFunds);
        this.categoryList = other.categoryList;
        Log.d("catListSize", "CAT LIST SIZE = " + categoryList.size());
        for (int i = 0; i < categoryList.size(); i++){
            categoryList.get(i).setCurrBalance(categoryList.get(i).getMaxBalance());
            categoryList.get(i).getExpenditureList().clear();
        }
    }

    //BudgetCategory toString, used to print all of a budget's info
    @Override
    public String toString() {
        String budgetInfo = "";
        for (int i=0; i < categoryList.size(); i++){
            BudgetCategory currCat = categoryList.get(i);
            String name = currCat.getName();
            float currAmt = currCat.getCurrBalance();
            float maxAmt = currCat.getMaxBalance();
            float spent = maxAmt-currAmt;

            String spentString = String.format(Locale.US,   "$%4.2f",spent);
            String currAmtString = String.format(Locale.US,"$%4.2f",currAmt);
            String maxAmtString= String.format(Locale.US,"$%4.2f",maxAmt);
            int maxLength = 8;

            budgetInfo += "--------+-------+--------+--------\n";
            budgetInfo += String.format("%-" +maxLength + "s|%-" + (maxLength - 1) + "s|%-"+ (maxLength - 1) + "s|%-"+ (maxLength - 1) + "s\n",name.substring(0, Math.min(name.length(), maxLength)), spentString.substring(0, Math.min(spentString.length(), maxLength)), currAmtString.substring(0, Math.min(currAmtString.length(), maxLength)), maxAmtString.substring(0, Math.min(maxAmtString.length(), maxLength)));
        }
        float totalSpent = totalMaxFunds-totalCurrFunds;
        String totalSpentString = String.format(Locale.US,"$%4.2f", totalSpent);
        String totalCurrString = String.format(Locale.US,"$%4.2f", totalCurrFunds);
        String totalMaxString = String.format(Locale.US,"$%4.2f", totalMaxFunds);
        int maxLength = 8;
        budgetInfo += "--------+-------+--------+--------\n";
        budgetInfo += String.format(Locale.US,"TOTAL:  |%-7s|%-7s|%-7s", totalSpentString.substring(0, Math.min(totalSpentString.length(), maxLength)), totalCurrString.substring(0, Math.min(totalCurrString.length(), maxLength)), totalMaxString.substring(0, Math.min(totalMaxString.length(), maxLength)));

        return budgetInfo;
    }
}
