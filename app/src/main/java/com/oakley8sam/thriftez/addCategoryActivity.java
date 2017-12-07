package com.oakley8sam.thriftez;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.oakley8sam.thriftez.BudgetClasses.Budget;
import com.oakley8sam.thriftez.BudgetClasses.BudgetCategory;
import com.oakley8sam.thriftez.Helpers.DecimalLimitFilter;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

public class addCategoryActivity extends AppCompatActivity {

    //instance variables, realm and category name and amount
    private EditText catName, catAmt;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        realm = Realm.getDefaultInstance();

        catName = (EditText) findViewById(R.id.newCategoryName);
        catAmt = (EditText) findViewById(R.id.newCategoryAmount);
        //limits the edit text to only 2 digits after the decimal place
        catAmt.setFilters(new InputFilter[] {new DecimalLimitFilter(3)});

    }

    //adds a new category to the budget, updating the realm database
    public void addNewCategory(View v){
        if(catName.getText().length() < 1 || catAmt.getText().toString().equals("")){
            Snackbar emptyField = Snackbar.make(catAmt, "Please fill out all fields", LENGTH_SHORT);
            emptyField.show();
            return;
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();

                float amt = Float.valueOf(catAmt.getText().toString());

                BudgetCategory catToAdd = bgrealm.createObject(BudgetCategory.class);
                catToAdd.setName(catName.getText().toString());
                catToAdd.setCurrBalance(amt);
                catToAdd.setMaxBalance(amt);

                Budget budgetToChange = budget.get(budget.size()-1);
                budgetToChange.addCategory(catToAdd);
            }
        });
        finish();
    }

    //overrides onDestroy to close realm
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
