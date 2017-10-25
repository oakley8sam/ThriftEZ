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

    //TODO: CREATE BUDGET GET CATEGORY FUNCTION AND REFACTOR REMOVE AND EDIT TO USE IT INSTEAD OF INNER FOR LOOP

    //removes a category from the list, decreasing max and current funds
    public boolean removeCategory(BudgetCategory cat){
        String nameToRemove = cat.getName();
        int listSize = categoryList.size();
        for(int i = 0; i < listSize; i++){
            if (categoryList.get(i).getName().equals(nameToRemove)) {
                BudgetCategory catToRemove = new BudgetCategory(categoryList.get(i));
                totalMaxFunds -= catToRemove.getMaxBalance();
                totalCurrFunds -= catToRemove.getCurrBalance();
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

    // returns the names of the categories as a ArrayList
    public ArrayList<String> getCatList() {
        ArrayList<String> catList = new ArrayList<String>();
        for (int i = 0; i< categoryList.size(); i++){
            catList.add(categoryList.get(i).getName());
        }
        return catList;
    }


    //BudgetCategory toString, used to print all of a budget's info
    @Override
    public String toString() {
        String budgetInfo = "";
        String toConcat = "";
        for (int i=0; i < categoryList.size(); i++){
            BudgetCategory currCat = categoryList.get(i);
            String name = currCat.getName();
            float currAmt = currCat.getCurrBalance();
            float maxAmt = currCat.getMaxBalance();
            float spent = maxAmt-currAmt;
            //TODO: FIND A BETTER FORMATTING METHOD
            budgetInfo += String.format("%-6s                     $%.2f                 $%.2f          $%.2f", name, spent, currAmt, maxAmt);
            budgetInfo += '\n';

        }
        float totalSpent = totalMaxFunds-totalCurrFunds;
        budgetInfo += String.format("TOTAL:            $%.2f              $%.2f              $%.2f", totalSpent, totalCurrFunds, totalMaxFunds);

        return budgetInfo;
    }
}
