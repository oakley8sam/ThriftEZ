package com.oakley8sam.thriftez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToRecord(View v){
        startActivity(new Intent(MainActivity.this, recordActivity.class));
    }

    public void goToExpenses(View v){
        //startActivity(new Intent(MainActivity.this, expensesActivity.class));
    }

    public void goToEdit(View v){
        startActivity(new Intent(MainActivity.this, editActivity.class));
    }

    /*public void goToCalendar(View v){
        startActivity(new Intent(MainActivity.this, calendarActivity.class));
    }*/
}
