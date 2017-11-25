package com.oakley8sam.thriftez;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.oakley8sam.thriftez.BudgetClasses.Budget;
import com.oakley8sam.thriftez.BudgetClasses.BudgetCategory;
import com.oakley8sam.thriftez.BudgetClasses.Expenditure;
import com.oakley8sam.thriftez.Helpers.DecimalLimitFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;


public class recordActivity extends AppCompatActivity {
    //instance variables, including spinner adapters, ArrayLists, and specific sets used to fill
    //the day spinner
    ArrayAdapter<String> categorySpinnerAdapter;
    ArrayAdapter<Integer> daySpinnerAdapter;
    ArrayAdapter<Integer> monthSpinnerAdapter;

    ArrayList<String> catList;
    ArrayList<Integer> daysList= new ArrayList<Integer>();

    Set<Integer> longMonths = new HashSet<Integer>(Arrays.asList(1, 3, 5, 7, 8, 10, 12));
    Set<Integer> shortMonths = new HashSet<Integer>(Arrays.asList(4, 6, 9, 11));

    private Realm realm;

    private EditText amtText, notesText, yearText;

    Spinner categorySpinner, daySpinner, monthSpinner;

    private Calendar cal = Calendar.getInstance();

    private Button recordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        realm = Realm.getDefaultInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yearText = (EditText) findViewById(R.id.yearField);
        yearText.setText(Integer.toString(cal.get(Calendar.YEAR)));

        //lock year to current year
        yearText.setEnabled(false);

        amtText = (EditText) findViewById(R.id.amountBox);
        //limits the amt to only two places after the decimal
        amtText.setFilters(new InputFilter[] {new DecimalLimitFilter(3)});

        notesText = (EditText) findViewById(R.id.notesBox);
        recordButton = (Button) findViewById(R.id.finishRecordButton);

        //loads the budget and category that will be changed
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();
                Budget budgetToChange = budget.get(budget.size()-1);
                catList = budgetToChange.getCatListString();

            }
        });

        //Creates and fills spinners with appropriate items.
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        categorySpinnerAdapter = new ArrayAdapter<String>
                                 (this, R.layout.spinner_item, catList);
        categorySpinnerAdapter.setDropDownViewResource
                               (android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);

        if(categorySpinnerAdapter.getCount() == 0){
            recordButton.setEnabled(false);
        }


        monthSpinner = (Spinner) findViewById(R.id.monthField);
        Integer[] monthList = {1,2,3,4,5,6,7,8,9,10,11,12};
        monthSpinnerAdapter = new ArrayAdapter<Integer>
                              (this, R.layout.spinner_item, monthList);
        monthSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthSpinnerAdapter);
        monthSpinner.setSelection(cal.get(Calendar.MONTH));

        //locks month to current month
        monthSpinner.setEnabled(false);


        daySpinner = (Spinner) findViewById(R.id.dayField);
        daySpinnerAdapter = new ArrayAdapter<Integer>
                            (this, R.layout.spinner_item, daysList);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daySpinnerAdapter);

        //populates the daySpinner with the appropriate days depending on month
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

        daySpinner.setSelection(cal.get(Calendar.DATE)-1);
    }

    //records an expense in the correct categories expenditure list, updating the budget info
    //in the process
    public void recordExpense(View v){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();

                Expenditure expenseToAdd = bgrealm.createObject(Expenditure.class);

                float amtSpent = Float.parseFloat(amtText.getText().toString());

                int day = Integer.parseInt(daySpinner.getSelectedItem().toString());
                int year = Integer.parseInt(yearText.getText().toString());
                int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());

                expenseToAdd.setAmtSpent(amtSpent);
                expenseToAdd.setDay(day);
                expenseToAdd.setYear(year);
                expenseToAdd.setNote(notesText.getText().toString());
                expenseToAdd.setMonth(month);

                Budget budgetToChange = budget.get(budget.size()-1);

                budgetToChange.addExpendituretoMaster(expenseToAdd);

                BudgetCategory catToChange = budgetToChange.getCatList()
                                             .get(categorySpinner.getSelectedItemPosition());
                catToChange.addExpenditure(expenseToAdd);
                budgetToChange.updateFunds();

            }
        });
        finish();
    }

    public void recordRepayment(View v){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();

                Expenditure repaymentToAdd = bgrealm.createObject(Expenditure.class);

                float amtPaid = Float.parseFloat(amtText.getText().toString());

                int day = Integer.parseInt(daySpinner.getSelectedItem().toString());
                int year = Integer.parseInt(yearText.getText().toString());
                int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());

                repaymentToAdd.setAmtSpent(amtPaid);
                repaymentToAdd.setDay(day);
                repaymentToAdd.setYear(year);
                repaymentToAdd.setNote(notesText.getText().toString());
                repaymentToAdd.setMonth(month);

                Budget budgetToChange = budget.get(budget.size()-1);

                BudgetCategory catToChange = budgetToChange.getCatList()
                        .get(categorySpinner.getSelectedItemPosition());
                catToChange.addRepayment(repaymentToAdd);
                budgetToChange.updateFunds();

            }
        });
        finish();
    }
}


