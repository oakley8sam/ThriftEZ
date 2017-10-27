package com.oakley8sam.thriftez;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.oakley8sam.thriftez.Model.Budget;
import com.oakley8sam.thriftez.Model.BudgetCategory;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class editCategoryActivity extends AppCompatActivity {

    ArrayAdapter<String> categorySpinnerAdapter;
    private Spinner updateSpinner;
    private Realm realm;
    private ArrayList<String> catList;
    private EditText newBalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        updateSpinner = (Spinner) findViewById(R.id.toUpdateSpinner);
        newBalText = (EditText) findViewById(R.id.newBalanceText);
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();
                Budget budgetToChange = budget.get(0);
                catList = budgetToChange.getCatListString();

                Log.d("length of catlist", "There are " + catList.size() + " cats");

            }
        });

        updateSpinner = (Spinner) findViewById(R.id.toUpdateSpinner);
        categorySpinnerAdapter = new ArrayAdapter<String> (this, R.layout.spinner_item, catList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerAdapter.notifyDataSetChanged();
        updateSpinner.setAdapter(categorySpinnerAdapter);
    }

    public void updateCategory(View v){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                float newBal = Float.parseFloat(newBalText.getText().toString());

                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();

                BudgetCategory catToRemove = bgrealm.createObject(BudgetCategory.class);
                catToRemove.setName(updateSpinner.getSelectedItem().toString());

                Budget budgetToChange = budget.get(0);
                budgetToChange.editCategory(catToRemove, newBal);
                catList.remove(updateSpinner.getSelectedItemPosition());

                Log.d("exiting delete", "Exiting deleteCategory");
            }
        });
        finish();
    }
}
