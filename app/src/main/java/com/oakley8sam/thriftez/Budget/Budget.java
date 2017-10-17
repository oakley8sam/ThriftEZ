package com.oakley8sam.thriftez.Budget;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import io.realm.RealmObject;

/**
 * Created by oakle on 10/17/2017.
 */

public class Budget extends RealmObject{
    //FOR SOME REASON WE CANT MAKE AN ARRAY LIST OF BUDGETCATEGORY 
    ArrayList<BudgetCategory> categoryList = new ArrayList<BudgetCategory>();
    float totalMaxFunds, totalCurrFunds;

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

}
