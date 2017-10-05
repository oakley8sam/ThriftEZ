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

public class recordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);


        Spinner monthSpinner = (Spinner) findViewById(R.id.monthField);
        ArrayAdapter monthSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        monthSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthSpinnerAdapter);

        Spinner daySpinner = (Spinner) findViewById(R.id.dayField);
        ArrayAdapter daySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.febDays, android.R.layout.simple_spinner_item);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daySpinnerAdapter);

        /*
        daySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ArrayAdapter daySpinnerAdapter = ArrayAdapter.createFromResource(recordActivity.this, R.array.longDays, android.R.layout.simple_spinner_item);
                return true;
            }
        });
        */
    }

}

