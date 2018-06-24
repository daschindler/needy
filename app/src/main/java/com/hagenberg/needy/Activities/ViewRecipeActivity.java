package com.hagenberg.needy.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

public class ViewRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        //RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        //Recipe selectedRecipe = recipeViewModel.getCurrentRecipeById(intent.getIntExtra("id", 404040));

        //Toast.makeText(this, selectedRecipe.getName(), Toast.LENGTH_LONG).show();
    }
}
