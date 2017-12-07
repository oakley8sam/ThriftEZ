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

    ArrayList<String> catList;
    ArrayList<Integer> daysList= new ArrayList<Integer>();

    private Realm realm;

    private EditText amtText, notesText;

    Spinner categorySpinner, daySpinner;

    private Calendar cal = Calendar.getInstance();

    private Button recordButton, repayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        realm = Realm.getDefaultInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        amtText = (EditText) findViewById(R.id.amountBox);
        //limits the amt to only two places after the decimal
        amtText.setFilters(new InputFilter[] {new DecimalLimitFilter(3)});

        notesText = (EditText) findViewById(R.id.notesBox);
        recordButton = (Button) findViewById(R.id.finishRecordButton);
        repayButton = (Button) findViewById(R.id.finishRepaymentButton);

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

        //disables buttons if the spinner is empty (meaning no categories to record to)
        if(categorySpinnerAdapter.getCount() == 0){
            recordButton.setEnabled(false);
            repayButton.setEnabled(false);
        }

        daySpinner = (Spinner) findViewById(R.id.dayField);
        daySpinnerAdapter = new ArrayAdapter<Integer>
                            (this, R.layout.spinner_item, daysList);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daySpinnerAdapter);

        //populates the daySpinner with the appropriate days depending on month
        daySpinnerAdapter.clear();
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= maxDay; i++)
            daySpinnerAdapter.add(i);

        daySpinner.setSelection(cal.get(Calendar.DATE) - 1);
    }

    //records an expense in the correct category's expenditure list, updating the budget info
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
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;

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

    //records a repayment in the correct category's repayment list, updating the budget info
    //in the process
    public void recordRepayment(View v){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();

                Expenditure repaymentToAdd = bgrealm.createObject(Expenditure.class);

                float amtPaid = Float.parseFloat(amtText.getText().toString());

                int day = Integer.parseInt(daySpinner.getSelectedItem().toString());
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;

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


