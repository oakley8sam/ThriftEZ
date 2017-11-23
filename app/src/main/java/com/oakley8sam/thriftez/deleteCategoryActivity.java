package com.oakley8sam.thriftez;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.oakley8sam.thriftez.Model.Budget;
import com.oakley8sam.thriftez.Model.BudgetCategory;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class deleteCategoryActivity extends AppCompatActivity {
    //instance variables, spinner and its adapter, realm instance, and a category array list,
    //and a button to be disabled if there is nothing to delete
    ArrayAdapter<String> categorySpinnerAdapter;
    private Spinner delSpinner;
    private Realm realm;
    private ArrayList<String> catList;
    private Button toDelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        delSpinner = (Spinner) findViewById(R.id.toDeleteSpinner);
        toDelButton = (Button) findViewById(R.id.deleteCategoryButton);
        realm = Realm.getDefaultInstance();

        //creates an ArrayList filled with the names of categories in the budgets, in order to
        //populate the spinner
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();
                Budget budgetToChange = budget.get(budget.size()-1);
                catList = budgetToChange.getCatListString();
            }
        });

        delSpinner = (Spinner) findViewById(R.id.toDeleteSpinner);
        categorySpinnerAdapter = new ArrayAdapter<String>
                                (this, R.layout.spinner_item, catList);
        categorySpinnerAdapter.setDropDownViewResource
                               (android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerAdapter.notifyDataSetChanged();
        delSpinner.setAdapter(categorySpinnerAdapter);

        if(categorySpinnerAdapter.getCount() == 0){
            toDelButton.setEnabled(false);
        }

    }

    //deletes the category corresponding to the name of the item in the spinner at the time of
    //button press
    public void deleteCategory(View v) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();

                BudgetCategory catToRemove = bgrealm.createObject(BudgetCategory.class);
                catToRemove.setName(delSpinner.getSelectedItem().toString());

                Budget budgetToChange = budget.get(budget.size()-1);
                budgetToChange.removeCategory(catToRemove);
                catList.remove(delSpinner.getSelectedItemPosition());
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
