package com.oakley8sam.thriftez;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.oakley8sam.thriftez.Model.Budget;
import com.oakley8sam.thriftez.Model.BudgetCategory;


import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class editActivity extends AppCompatActivity {
    private Realm realm;
    private Calendar cal = Calendar.getInstance();
    private TableLayout budgetInfoTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        realm = Realm.getDefaultInstance();

        budgetInfoTable = (TableLayout) findViewById(R.id.budgetInfoTable);

        //creates new budgets on first use of the app, or first use after new month, saves
        //to realm database
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                RealmResults<Budget> budgets = realm.where(Budget.class).findAll();
                //creates a clean budget when the app is first opened
                if(budgets.size() == 0) {
                    Budget budget = bgRealm.createObject(Budget.class);
                    budget.setTotalCurrFunds(0);
                    budget.setTotalMaxFunds(0);
                    budget.setMonthCreated(cal.get(Calendar.MONTH));
                    budget.setYearCreated(cal.get(Calendar.YEAR));
                }

                //creates a new budget on a new month, based on the past month's budget
                if(budgets.get(budgets.size()-1).getMonthCreated() != cal.get(Calendar.MONTH)){
                    Budget budget = bgRealm.createObject(Budget.class);
                    budget.duplicateBudget(budgets.get(budgets.size()-2));
                    budget.setMonthCreated(cal.get(Calendar.MONTH));
                    budget.setYearCreated(cal.get(Calendar.YEAR));
                }
            }
        });
    }

    @Override
    //overrides onResume to refresh the budget info table
    protected void onResume(){
        super.onResume();

        int rowCount = budgetInfoTable.getChildCount();

        //clears all but the header row
        if(rowCount > 1){
            budgetInfoTable.removeViews(1,rowCount - 1);
        }

        //rebuilds the budget info table
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budgets = realm.where(Budget.class).findAll();
                Budget budgetToPrint = budgets.get(budgets.size()-1);
                RealmList<BudgetCategory> categoriesToPrint = budgetToPrint.getCatList();

                //builds table row by row, adding individual textviews to each row, then
                //the entire row to the table
                TableRow totalRow = new TableRow(budgetInfoTable.getContext());
                totalRow.setLayoutParams(new TableRow.LayoutParams
                                                (ActionBar.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT));

                //refreshes the total row
                TextView total = new TextView(budgetInfoTable.getContext());
                total.setText("TOTAL");
                total.setTextColor(getResources().getColor(R.color.colorBlack));
                totalRow.addView(total);

                TextView totalSpent = new TextView(budgetInfoTable.getContext());
                float tspent = budgets.get
                        (budgets.size()-1).getTotalMaxFunds() -
                         budgets.get(budgets.size()-1).getTotalCurrFunds();
                String totalSpentString = Float.toString(tspent);
                totalSpent.setText((totalSpentString));
                totalSpent.setTextColor(getResources().getColor(R.color.colorRed));
                totalRow.addView(totalSpent);

                TextView totalCurr = new TextView(budgetInfoTable.getContext());
                totalCurr.setText(Float.toString(budgetToPrint.getTotalCurrFunds()));
                totalCurr.setTextColor(getResources().getColor(R.color.colorBlack));
                totalRow.addView(totalCurr);

                TextView totalMax = new TextView(budgetInfoTable.getContext());
                totalMax.setText(Float.toString(budgetToPrint.getTotalMaxFunds()));
                totalMax.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                totalRow.addView(totalMax);

                budgetInfoTable.addView(totalRow, new TableLayout.LayoutParams
                                               (ViewGroup.LayoutParams.MATCH_PARENT,
                                               ViewGroup.LayoutParams.WRAP_CONTENT));

                int catsToPrintSize = categoriesToPrint.size();

                //refreshes the individual category information row by row
                for (int i = 0; i < catsToPrintSize; i++){
                    TableRow tr = new TableRow(budgetInfoTable.getContext());
                    tr.setLayoutParams(new TableRow.LayoutParams
                                      (ActionBar.LayoutParams.MATCH_PARENT,
                                       ViewGroup.LayoutParams.WRAP_CONTENT));

                    BudgetCategory currCategory = categoriesToPrint.get(i);
                    TextView catName = new TextView(budgetInfoTable.getContext());
                    catName.setText(currCategory.getName());
                    //truncates the category name if it is too long to fit into the table
                    if (currCategory.getName().length() > 10){
                        String truncatedName = currCategory.getName().substring(0,7);
                        truncatedName += "...";
                        catName.setText(truncatedName);
                    }
                    catName.setTextColor(getResources().getColor(R.color.colorBlack));
                    tr.addView(catName);

                    TextView spentVal = new TextView(budgetInfoTable.getContext());
                    float spent = currCategory.getMaxBalance() - currCategory.getCurrBalance();
                    String spentString = Float.toString(spent);
                    spentVal.setText(spentString);
                    spentVal.setTextColor(getResources().getColor(R.color.colorRed));
                    tr.addView(spentVal);

                    TextView currVal = new TextView(budgetInfoTable.getContext());
                    currVal.setText(Float.toString(currCategory.getCurrBalance()));
                    currVal.setTextColor(getResources().getColor(R.color.colorBlack));
                    tr.addView(currVal);

                    TextView maxVal = new TextView(budgetInfoTable.getContext());
                    maxVal.setText(Float.toString(currCategory.getMaxBalance()));
                    maxVal.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    tr.addView(maxVal);

                    budgetInfoTable.addView(tr, new TableLayout.LayoutParams
                                                   (ViewGroup.LayoutParams.MATCH_PARENT,
                                                   ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
        });
    }


    //goes to record activity
    public void goToRecord(View v){
        Intent intent = new Intent(editActivity.this, recordActivity.class);
        startActivity(intent);
    }

    //goes to expenses activity
    public void goToExpenses(View v){
        startActivity(new Intent(editActivity.this, expensesActivity.class));
    }

    //goes to calendar activity
    public void goToCalendar(View v){
        startActivity(new Intent(editActivity.this, calendarActivity.class));
    }

    //goes to deleteCategory activity
    public void goToDeleteCategory(View v){
        Intent intent = new Intent(editActivity.this, deleteCategoryActivity.class);
        startActivity(intent);
    }

    //goes to addCategory Activity
    public void goToAddCategory(View v){
        startActivity(new Intent(editActivity.this, addCategoryActivity.class));
    }

    //goes to editCategory activity
    public void goToEditCategory(View v){
        startActivity(new Intent(editActivity.this, editCategoryActivity.class));
    }


    //overrides onStop to close realm
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


}
