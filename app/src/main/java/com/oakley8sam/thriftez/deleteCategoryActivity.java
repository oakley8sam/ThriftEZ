package com.oakley8sam.thriftez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.oakley8sam.thriftez.Model.Budget;
import com.oakley8sam.thriftez.Model.BudgetCategory;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class deleteCategoryActivity extends AppCompatActivity {
    //instance variables
    ArrayAdapter<String> categorySpinnerAdapter;
    private Spinner delSpinner;
    private Realm realm;
    private ArrayList<String> catList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        delSpinner = (Spinner) findViewById(R.id.toDeleteSpinner);
        realm = Realm.getDefaultInstance();

        //creates an ArrayList filled with the names of categories in the budgets, in order to
        //populate the spinner
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();
                Budget budgetToChange = budget.get(0);
                catList = budgetToChange.getCatList();

            }
        });

        Spinner delSpinner = (Spinner) findViewById(R.id.toDeleteSpinner);
        categorySpinnerAdapter = new ArrayAdapter<String> (this, R.layout.spinner_item, catList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        delSpinner.setAdapter(categorySpinnerAdapter);

    }


    //deletes the category corresponding to the name of the item in the spinner at the time of
    //button press
    public void deleteCategory(View v) {
        Log.d("in deleteCategory", "Beginning deleteCategory");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                Log.d("before results", "Before RealmResults");
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();

                BudgetCategory catToRemove = bgrealm.createObject(BudgetCategory.class);
                catToRemove.setName(delSpinner.getSelectedItem().toString());

                Budget budgetToChange = budget.get(0);
                budgetToChange.removeCategory(catToRemove);
                catList.remove(delSpinner.getSelectedItemPosition());
                Log.d("exiting delete", "Exiting deleteCategory");
            }
        });
    }

    //overrides onDestroy to close realm
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
