package com.oakley8sam.thriftez;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class recordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //////////////////////
        ArrayList<String> categoryList = new ArrayList<String>();

        categoryList.add("Food");
        categoryList.add("Rent");
        //////////////////////

        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String> (this, R.layout.spinner_item, categoryList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);


        Spinner monthSpinner = (Spinner) findViewById(R.id.monthField);
        ArrayAdapter monthSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.months, R.layout.spinner_item);
        monthSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthSpinnerAdapter);

        Spinner daySpinner = (Spinner) findViewById(R.id.dayField);
        final ArrayAdapter daySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.febDays, R.layout.spinner_item);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daySpinnerAdapter);

        /*
        daySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                daySpinnerAdapter.
                ArrayAdapter daySpinnerAdapter = ArrayAdapter.createFromResource(recordActivity.this, R.array.longDays, android.R.layout.simple_spinner_item);
                return true;
            }
        });
        */
    }

}

