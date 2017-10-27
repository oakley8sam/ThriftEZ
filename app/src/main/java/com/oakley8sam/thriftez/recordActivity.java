package com.oakley8sam.thriftez;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.oakley8sam.thriftez.Model.Budget;
import com.oakley8sam.thriftez.Model.BudgetCategory;
import com.oakley8sam.thriftez.Model.Expenditure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;


//TODO: CLEAN CLASS
public class recordActivity extends AppCompatActivity {
    //instance variables, including spinner adapters, ArrayLists, and specific sets used to fill
    //the day spinner
    ArrayAdapter<String> categorySpinnerAdapter;
    ArrayAdapter<Integer> daySpinnerAdapter;
    ArrayAdapter monthSpinnerAdapter;

    ArrayList<String> catList;
    ArrayList<Integer> daysList= new ArrayList<Integer>();

    Set<String> longMonths = new HashSet<String>(Arrays.asList("January", "March", "May", "July",
                                                  "August", "October", "December"));
    Set<String> shortMonths = new HashSet<String>(Arrays.asList("April", "June", "September", "November"));

    private Realm realm;

    private EditText amtText, notesText, yearText;

    Spinner categorySpinner, daySpinner, monthSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        realm = Realm.getDefaultInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yearText = (EditText) findViewById(R.id.yearField);
        amtText = (EditText) findViewById(R.id.amountBox);
        notesText = (EditText) findViewById(R.id.notesBox);

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

        //Creates and fills spinners with appropriate items.
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        categorySpinnerAdapter = new ArrayAdapter<String> (this, R.layout.spinner_item, catList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);


        monthSpinner = (Spinner) findViewById(R.id.monthField);
        monthSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.months, R.layout.spinner_item);
        monthSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthSpinnerAdapter);


        daySpinner = (Spinner) findViewById(R.id.dayField);
        daySpinnerAdapter = new ArrayAdapter<Integer> (this, R.layout.spinner_item, daysList);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daySpinnerAdapter);

        //uses an onTouchListener to fill day spinner with appropriate values based on month and
        //year at the time of the spinner's opening.
        daySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                daySpinnerAdapter.clear();
                int max = 28;
                String yearString = yearText.getText().toString();
                int yearInt = Integer.parseInt(yearString);
                if (longMonths.contains(monthSpinner.getSelectedItem()))
                    max = 31;
                else if(shortMonths.contains(monthSpinner.getSelectedItem()))
                    max = 30;
                else if(yearInt % 4 == 0) {
                    if(yearInt % 100 == 0) {
                        if (yearInt % 400 == 0)
                            max = 29;
                    } else
                        max = 29;
                }
                for (int i = 1; i <= max; i++)
                    daySpinnerAdapter.add(i);
                return false;
            }
        });
    }

    //records an expense in the correct categories expenditure list, updating the budget info
    //in the process
    //TODO: GET THIS WORKING
    public void recordExpense(View v){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                Log.d("before results", "Before RealmResults");
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();


                Expenditure expenseToAdd = bgrealm.createObject(Expenditure.class);
                float amtSpent = Float.parseFloat(amtText.getText().toString());
                int day = Integer.parseInt(daySpinner.getSelectedItem().toString());
                int year = Integer.parseInt(yearText.getText().toString());
                expenseToAdd.setAmtSpent(amtSpent);
                expenseToAdd.setDay(day);
                expenseToAdd.setYear(year);
                expenseToAdd.setNote(notesText.getText().toString());
                expenseToAdd.setMonth(monthSpinner.getSelectedItem().toString());

                Log.d("toString test", expenseToAdd.toString());

                Budget budgetToChange = budget.get(0);

                int budgetSize = budgetToChange.getCatList().size();
                for (int i = 0; i<budgetSize; i++){
                    Log.d("for loop test", categorySpinner.getSelectedItem().toString());
                    Log.d("for loop test again", budgetToChange.getCatListString().get(i));
                    if (categorySpinner.getSelectedItem().toString().equals(budgetToChange.getCatListString().get(i))){
                        budgetToChange.getCatList().get(i).addExpenditure(expenseToAdd);
                        budgetToChange.updateFunds();
                        finish();
                    }
                }

            }
        });
        finish();
    }
}


