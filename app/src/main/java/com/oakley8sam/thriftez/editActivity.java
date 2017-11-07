package com.oakley8sam.thriftez;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.oakley8sam.thriftez.Model.Budget;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class editActivity extends AppCompatActivity {
    private TextView budgetInfoText;
    private Budget budg;
    private Realm realm;

    ////
    private Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();

        budgetInfoText = (TextView) findViewById(R.id.budgetInfoText);
        budgetInfoText.setMovementMethod(new ScrollingMovementMethod());
        budgetInfoText.setTextScaleX(1.5f);
        budgetInfoText.setTextSize(12);
        budgetInfoText.setTypeface(Typeface.MONOSPACE);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

               //realm.deleteAll();

                RealmResults<Budget> budgets = realm.where(Budget.class).findAll();
                Log.d("SIZE", "SIZE: " + budgets.size());
                //creates a clean budget when the app is first opened
                if(budgets.size() == 0) {
                    Budget budget = bgRealm.createObject(Budget.class);
                    budget.setTotalCurrFunds(0);
                    budget.setTotalMaxFunds(0);

                    budget.setMonthCreated(cal.get(Calendar.MONTH));
                }

                //creates a new budget on a new month, based on the past month's budget
                if(budgets.get(budgets.size()-1).getMonthCreated() != cal.get(Calendar.MONTH)){
                    Log.d("new month budget", "CREATING BUDGET FOR NEW MONTH");
                    Budget budget = bgRealm.createObject(Budget.class);

                    budget.duplicateBudget(budgets.get(budgets.size()-2));
                    budget.setMonthCreated(cal.get(Calendar.MONTH));
                    budget.setYearCreated(cal.get(Calendar.YEAR));
                }
                Log.v("number of budgets", "There are: " + budgets.size() + " budgets");
            }
        });


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();
                /////////////////////////////////////////////
                Budget budgetToPrint = budget.get(budget.size()-1);
                budg = budgetToPrint;
            }
        });

        budgetInfoText.setText(budg.toString());
    }

    @Override
    protected void onResume(){
        super.onResume();
        budgetInfoText.setText(budg.toString());
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

    //goes to calendar activity (currently non-functioning)
    /*public void goToCalendar(View v){
        startActivity(new Intent(MainActivity.this, calendarActivity.class));
    }*/

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
