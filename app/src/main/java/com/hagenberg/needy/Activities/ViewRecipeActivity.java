package com.hagenberg.needy.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hagenberg.needy.R;

public class ViewRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
