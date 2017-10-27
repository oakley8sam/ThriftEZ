package com.oakley8sam.thriftez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.oakley8sam.thriftez.Model.Budget;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    //creates realm instance for use in this class
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiates realm for use in this class
        realm = Realm.getDefaultInstance();

        //adds a budget to the realm if one does not already exist

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                RealmResults<Budget> budgets = realm.where(Budget.class).findAll();
                if(budgets.size() != 1) {
                    Budget budget = bgRealm.createObject(Budget.class);
                    budget.setTotalCurrFunds(0);
                    budget.setTotalMaxFunds(0);
                }
                Log.d("number of budgets", "There are: " + budgets.size() + " budgets");
            }
        });

    }

    //goes to record activity
    public void goToRecord(View v){
        Intent intent = new Intent(MainActivity.this, recordActivity.class);
        startActivity(intent);
    }

    //goes to expenses activity
     public void goToExpenses(View v){
        startActivity(new Intent(MainActivity.this, expensesActivity.class));
    }

    //goes to edit activity
    public void goToEdit(View v){
        Intent intent = new Intent(MainActivity.this, editActivity.class);
        startActivity(intent);
    }

    //goes to calendar activity (currently non-functioning)
    /*public void goToCalendar(View v){
        startActivity(new Intent(MainActivity.this, calendarActivity.class));
    }*/

    //overrides onStop to close realm
    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }

    //overrides onStop to close realm
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
