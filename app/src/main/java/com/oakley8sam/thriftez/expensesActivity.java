package com.oakley8sam.thriftez;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.oakley8sam.thriftez.BudgetClasses.Budget;
import com.oakley8sam.thriftez.BudgetClasses.BudgetCategory;
import com.oakley8sam.thriftez.BudgetClasses.Expenditure;

import java.text.DecimalFormat;
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
    private Button viewButton;
    private TableLayout expenditureInfoTable;

    //decimal format used to print monetary values with 2 decimal places
    DecimalFormat df = new DecimalFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        viewSpinner = (Spinner) findViewById(R.id.toDeleteSpinner);
        realm = Realm.getDefaultInstance();
        viewButton = (Button) findViewById(R.id.viewCategoryButton);
        expenditureInfoTable = (TableLayout) findViewById(R.id.expenseInfoTable);

        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

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

        //disables button if the spinner is empty (meaning no categories to list)
        if(categorySpinnerAdapter.getCount() == 0){
            viewButton.setEnabled(false);
        }

    }

    //prints the catgory's expenses and repayments to a table
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
                    amtSpent.setText(df.format(currExp.getAmtSpent()));
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

                RealmList<Expenditure> repayments = catToPrint.getRepaymentList();
                int numRepayments = repayments.size();

                //repopulates the table with repayment information row by row
                for(int i = 0; i < numRepayments; i++){
                    TableRow tr = new TableRow(expenditureInfoTable.getContext());
                    tr.setLayoutParams(new TableRow.LayoutParams
                                      (ActionBar.LayoutParams.MATCH_PARENT,
                                       ViewGroup.LayoutParams.WRAP_CONTENT));

                    Expenditure currRepayment = repayments.get(i);

                    TextView amtPaid = new TextView(expenditureInfoTable.getContext());
                    amtPaid.setText(df.format(currRepayment.getAmtSpent()));
                    amtPaid.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    tr.addView(amtPaid);

                    TextView repayDate = new TextView(expenditureInfoTable.getContext());
                    repayDate.setText(currRepayment.getMonth() + "/" +
                            currRepayment.getDay() + "/" + currRepayment.getYear());
                    repayDate.setTextColor(getResources().getColor(R.color.colorBlack));
                    tr.addView(repayDate);

                    TextView repayNote = new TextView(expenditureInfoTable.getContext());
                    repayNote.setText(currRepayment.getNote());
                    repayNote.setTextColor(getResources().getColor(R.color.colorBlack));
                    tr.addView(repayNote);

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

