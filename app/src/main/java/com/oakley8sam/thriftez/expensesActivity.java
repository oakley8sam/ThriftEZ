package com.oakley8sam.thriftez;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.oakley8sam.thriftez.Model.Budget;
import com.oakley8sam.thriftez.Model.BudgetCategory;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class expensesActivity extends AppCompatActivity {
    ArrayAdapter<String> categorySpinnerAdapter;
    private Spinner viewSpinner;
    private Realm realm;
    private ArrayList<String> catList;
    private TextView categoryExpenseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        viewSpinner = (Spinner) findViewById(R.id.toDeleteSpinner);
        realm = Realm.getDefaultInstance();

        //creates an ArrayList filled with the names of categories in the budgets, in order to
        //populate the spinner
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();
                /////////////////////////////////////////////
                Budget budgetToView = budget.get(budget.size()-1);
                catList = budgetToView.getCatListString();

                Log.d("length of catlist", "There are " + catList.size() + " cats");

            }
        });

        viewSpinner = (Spinner) findViewById(R.id.toViewSpinner);
        categorySpinnerAdapter = new ArrayAdapter<String> (this, R.layout.spinner_item, catList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerAdapter.notifyDataSetChanged();
        viewSpinner.setAdapter(categorySpinnerAdapter);

        categoryExpenseText = (TextView) findViewById(R.id.expensesText);

    }

    public void viewCategoryExpenses(View v){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();
                ////////////////////////////////////////////////
                Budget budgetToPrint = budget.get(budget.size()-1);

                int budgetSize = budgetToPrint.getCatList().size();
                for (int i = 0; i<budgetSize; i++){
                    if (viewSpinner.getSelectedItem().toString().equals(budgetToPrint.getCatList().get(i).getName())){
                        int expListLen = budgetToPrint.getCatList().get(i).getExpenditureList().size();
                        String expendituresInfo = "";
                        for (int j = 0; j < expListLen; j++){
                            expendituresInfo+=budgetToPrint.getCatList().get(i).getExpenditureList().get(j).toString();
                        }
                        categoryExpenseText.setText(expendituresInfo);
                        Log.d("toString test again", budgetToPrint.getCatList().get(i).toString());
                    }
                }
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

