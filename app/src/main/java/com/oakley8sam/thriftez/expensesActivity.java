package com.oakley8sam.thriftez;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.oakley8sam.thriftez.Model.Budget;
import com.oakley8sam.thriftez.Model.BudgetCategory;
import com.oakley8sam.thriftez.Model.Expenditure;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class expensesActivity extends AppCompatActivity {
    //instance variables, spinners, spinner adapters, realm instance, tablelayout and string
    //of categories
    ArrayAdapter<String> categorySpinnerAdapter;
    private Spinner viewSpinner;
    private Realm realm;
    private ArrayList<String> catList;
    private TableLayout expenditureInfoTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        viewSpinner = (Spinner) findViewById(R.id.toDeleteSpinner);
        realm = Realm.getDefaultInstance();
        expenditureInfoTable = (TableLayout) findViewById(R.id.expenseInfoTable);

        //creates an ArrayList filled with the names of categories in the budgets, in order to
        //populate the spinner
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();
                Budget budgetToView = budget.get(budget.size()-1);
                catList = budgetToView.getCatListString();
            }
        });

        viewSpinner = (Spinner) findViewById(R.id.toViewSpinner);
        categorySpinnerAdapter = new ArrayAdapter<String>
                                (this, R.layout.spinner_item, catList);
        categorySpinnerAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerAdapter.notifyDataSetChanged();
        viewSpinner.setAdapter(categorySpinnerAdapter);

    }

    public void viewCategoryExpenses(View v){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budgets = realm.where(Budget.class).findAll();
                budgets.load();
                Budget budgetToPrint = budgets.get(budgets.size()-1);

                BudgetCategory catToPrint = budgetToPrint.getCatList()
                                            .get(viewSpinner.getSelectedItemPosition());

                int rowCount = expenditureInfoTable.getChildCount();

                //clears all but the header row
                if(rowCount > 1){
                    expenditureInfoTable.removeViews(1,rowCount - 1);
                }

                RealmList<Expenditure> expenses = catToPrint.getExpenditureList();
                int numExpenses = expenses.size();

                //repopulates the table with expenditure information row by row
                for(int i = 0; i < numExpenses; i++){
                    TableRow tr = new TableRow(expenditureInfoTable.getContext());
                    tr.setLayoutParams(new TableRow.LayoutParams
                                                (ActionBar.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT));

                    Expenditure currExp = expenses.get(i);

                    TextView amtSpent = new TextView(expenditureInfoTable.getContext());
                    amtSpent.setText(Float.toString(currExp.getAmtSpent()));
                    amtSpent.setTextColor(getResources().getColor(R.color.colorRed));
                    tr.addView(amtSpent);

                    TextView expDate = new TextView(expenditureInfoTable.getContext());
                    expDate.setText(currExp.getMonth() + "/" +
                                    currExp.getDay() + "/" + currExp.getYear());
                    expDate.setTextColor(getResources().getColor(R.color.colorBlack));
                    tr.addView(expDate);

                    TextView expNote = new TextView(expenditureInfoTable.getContext());
                    expNote.setText(currExp.getNote());
                    expNote.setTextColor(getResources().getColor(R.color.colorBlack));
                    tr.addView(expNote);

                    expenditureInfoTable.addView(tr, new TableLayout.LayoutParams
                                                        (ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT));
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

