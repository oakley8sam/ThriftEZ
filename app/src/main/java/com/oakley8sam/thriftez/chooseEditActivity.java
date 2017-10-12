package com.oakley8sam.thriftez;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class chooseEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_edit);
    }

    public void goToDeleteCategory(View v){
        startActivity(new Intent(chooseEditActivity.this, deleteCategoryActivity.class));
    }

    public void goToAddCategory(View v){
        startActivity(new Intent(chooseEditActivity.this, addCategoryActivity.class));
    }

    public void goToEditCategory(View v){
        startActivity(new Intent(chooseEditActivity.this, editCategoryActivity.class));
    }
}