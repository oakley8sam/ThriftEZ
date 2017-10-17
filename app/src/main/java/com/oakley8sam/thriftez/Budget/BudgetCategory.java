package com.oakley8sam.thriftez.Budget;

import java.util.ArrayList;

/**
 * Created by oakle on 10/17/2017.
 */

public class BudgetCategory {
    String name;
    float startingBalance, currBalance;
    ArrayList<String> expenditureList = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(float startingBalance) {
        this.startingBalance = startingBalance;
    }

    public float getCurrBalance() {
        return currBalance;
    }

    public void setCurrBalance(float currBalance) {
        this.currBalance = currBalance;
    }
}
