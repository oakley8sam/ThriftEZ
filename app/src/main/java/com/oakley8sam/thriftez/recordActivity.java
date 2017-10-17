package com.oakley8sam.thriftez;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class recordActivity extends AppCompatActivity {
    ArrayAdapter<String> categorySpinnerAdapter;
    ArrayAdapter<Integer> daySpinnerAdapter;
    ArrayAdapter monthSpinnerAdapter;

    Intent intent = new Intent();
    ArrayList<String> categoryList = new ArrayList<String>();
    ArrayList<Integer> daysList= new ArrayList<Integer>();

    Set<String> longMonths = new HashSet<String>(Arrays.asList("January", "March", "May", "July",
                                                  "August", "October", "December"));
    Set<String> shortMonths = new HashSet<String>(Arrays.asList("April", "June", "September", "November"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText yearText = (EditText) findViewById(R.id.yearField);

        categoryList.add("Food");
        categoryList.add("Rent");

        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        categorySpinnerAdapter = new ArrayAdapter<String> (this, R.layout.spinner_item, categoryList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);


        final Spinner monthSpinner = (Spinner) findViewById(R.id.monthField);
        monthSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.months, R.layout.spinner_item);
        monthSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthSpinnerAdapter);


        Spinner daySpinner = (Spinner) findViewById(R.id.dayField);
        daySpinnerAdapter = new ArrayAdapter<Integer> (this, R.layout.spinner_item, daysList);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daySpinnerAdapter);

        daySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                daySpinnerAdapter.clear();
                int max = 28;
                String yearString = yearText.getText().toString();
                int yearInt = Integer.parseInt(yearString);
                if (longMonths.contains(monthSpinner.getSelectedItem()))
                    max = 31;
                else if(shortMonths.contains(monthSpinner.getSelectedItem()))
                    max = 30;
                else if(yearInt % 4 == 0) {
                    if(yearInt % 100 == 0) {
                        if (yearInt % 400 == 0)
                            max = 29;
                    } else
                        max = 29;
                }
                for (int i = 1; i <= max; i++)
                    daySpinnerAdapter.add(i);
                return false;
            }
        });
    }
}


