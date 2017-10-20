package com.oakley8sam.thriftez.Model;

import android.util.Log;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by oakle on 10/17/2017.
 */

public class Budget extends RealmObject{

    //instance variables, a list of categories, and floats for max and current funds
    RealmList<BudgetCategory> categoryList = new RealmList<BudgetCategory>();
    float totalMaxFunds, totalCurrFunds;

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

    //updates max and curr funds based on max and curr funds for each category
    public void updateFunds(){
        for (int i=0; i<categoryList.size();i++){
            totalMaxFunds+=categoryList.get(i).getMaxBalance();
            totalCurrFunds+=categoryList.get(i).getCurrBalance();
        }
    }

    //adds a category to the list, increasing max and current funds
    public void addCategory(BudgetCategory cat){
        categoryList.add(categoryList.size(),cat);
        totalCurrFunds+=cat.getCurrBalance();
        totalMaxFunds+=cat.getMaxBalance();
    }

    //removes a category from the list, decreasing max and current funds
    public boolean removeCategory(BudgetCategory cat){
        BudgetCategory catToRemove = null;
        for(int i = 0; i < categoryList.size(); i++){
            if (categoryList.get(i).getName() == cat.getName()){
                catToRemove = categoryList.get(i);
                break;
            }

        }
        if (catToRemove == null)
            return false;

        totalMaxFunds -= catToRemove.getMaxBalance();
        totalCurrFunds -= catToRemove.getCurrBalance();
        categoryList.remove(catToRemove);

        return true;
    }

    // returns the names of the categories as a ArrayList
    public ArrayList<String> getCatList() {
        ArrayList<String> catList = new ArrayList<String>();
        for (int i = 0; i< categoryList.size(); i++){
            catList.add(categoryList.get(i).getName());
        }
        return catList;
    }
}
