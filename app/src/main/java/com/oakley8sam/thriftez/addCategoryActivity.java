package com.oakley8sam.thriftez;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.oakley8sam.thriftez.Model.Budget;
import com.oakley8sam.thriftez.Model.BudgetCategory;

import io.realm.Realm;
import io.realm.RealmResults;

public class addCategoryActivity extends AppCompatActivity {

    //instance variabes, realm and category name and amount
    private EditText catName, catAmt;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        realm = Realm.getDefaultInstance();

        catName = (EditText) findViewById(R.id.newCategoryName);
        catAmt = (EditText) findViewById(R.id.newCategoryAmount);
    }

    //adds a new category to the budget, updating the realm database
    public void addNewCategory(View v){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                Log.d("before results", "Before RealmResults");
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();


                float amt = Float.valueOf(catAmt.getText().toString());
                BudgetCategory catToAdd = bgrealm.createObject(BudgetCategory.class);
                catToAdd.setName(catName.getText().toString());
                catToAdd.setCurrBalance(amt);
                catToAdd.setMaxBalance(amt);

                //////////////////////////////////////
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
