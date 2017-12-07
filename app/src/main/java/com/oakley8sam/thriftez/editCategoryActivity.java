package com.oakley8sam.thriftez;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.oakley8sam.thriftez.BudgetClasses.Budget;
import com.oakley8sam.thriftez.BudgetClasses.BudgetCategory;
import com.oakley8sam.thriftez.Helpers.DecimalLimitFilter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

public class editCategoryActivity extends AppCompatActivity {

    //instance variables, a spinner and its adapter, a realm instance, a category array list,
    //an edit text, and a button to disable the submit button if necessary
    ArrayAdapter<String> categorySpinnerAdapter;
    private Spinner updateSpinner;
    private Realm realm;
    private ArrayList<String> catList;
    private EditText newBalText, newNameText;
    private ImageButton updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        updateSpinner = (Spinner) findViewById(R.id.toUpdateSpinner);

        newNameText = (EditText) findViewById(R.id.newNameText);

        newBalText = (EditText) findViewById(R.id.newBalanceText);
        //limits the newBal to only two places after the decimal
        newBalText.setFilters(new InputFilter[] {new DecimalLimitFilter(3)});

        updateButton = (ImageButton) findViewById(R.id.updateCategoryButton);
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();
                Budget budgetToChange = budget.get(budget.size()-1);
                catList = budgetToChange.getCatListString();
            }
        });

        updateSpinner = (Spinner) findViewById(R.id.toUpdateSpinner);
        categorySpinnerAdapter = new ArrayAdapter<String>
                                (this, R.layout.spinner_item, catList);
        categorySpinnerAdapter.setDropDownViewResource
                               (android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerAdapter.notifyDataSetChanged();
        updateSpinner.setAdapter(categorySpinnerAdapter);

        //disables the button if the spinner is empty(meaning there are no categories to edit)
        if(categorySpinnerAdapter.getCount() == 0){
            updateButton.setEnabled(false);
        }
    }

    //updates the category's info based on the values in the ui fields
    public void updateCategory(View v){
        if(newBalText.getText().toString().equals("") || newNameText.getText().length()<1){
            Snackbar emptyField = Snackbar.make(newBalText, "Please fill out all fields", LENGTH_SHORT);
            emptyField.show();
            return;
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                float newBal = Float.parseFloat(newBalText.getText().toString());
                String newName = newNameText.getText().toString();

                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();

                BudgetCategory catToEdit = bgrealm.createObject(BudgetCategory.class);
                catToEdit.setName(updateSpinner.getSelectedItem().toString());

                Budget budgetToChange = budget.get(budget.size()-1);
                budgetToChange.editCategory(catToEdit, newName, newBal);
            }
        });
        finish();
    }
}
