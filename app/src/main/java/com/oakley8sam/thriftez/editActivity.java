package com.oakley8sam.thriftez;

import android.content.Intent;
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

import io.realm.Realm;
import io.realm.RealmResults;

public class editActivity extends AppCompatActivity {
    private TextView budgetInfoText;
    private Budget budg;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();

        budgetInfoText = (TextView) findViewById(R.id.budgetInfoText);
        budgetInfoText.setMovementMethod(new ScrollingMovementMethod());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                RealmResults<Budget> budget = realm.where(Budget.class).findAll();
                budget.load();
                Budget budgetToPrint = budget.get(0);
                budg = budgetToPrint;
            }
        });

        budgetInfoText.setText(budg.toString());
    }

    public void goToChooseEdit(View v){
        Intent intent = new Intent(editActivity.this, chooseEditActivity.class);
        startActivity(intent);
    }

}
