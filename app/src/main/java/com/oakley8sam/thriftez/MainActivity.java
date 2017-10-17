package com.oakley8sam.thriftez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    //ArrayList<String> categoryList = new ArrayList<String>();

    //Declare Realm
    public Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Config for Database
        realm = Realm.getDefaultInstance();

    }

    public void goToRecord(View v){
        Intent intent = new Intent(MainActivity.this, recordActivity.class);
        //intent.putStringArrayListExtra("catList", categoryList);
        startActivity(intent);
    }

    /* public void goToExpenses(View v){
        //startActivity(new Intent(MainActivity.this, expensesActivity.class));
    } */

    public void goToEdit(View v){
        Intent intent = new Intent(MainActivity.this, editActivity.class);
        //intent.putStringArrayListExtra("catList", categoryList);
        startActivity(intent);
    }

    /*public void goToCalendar(View v){
        startActivity(new Intent(MainActivity.this, calendarActivity.class));
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
