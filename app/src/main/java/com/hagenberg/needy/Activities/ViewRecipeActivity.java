package com.hagenberg.needy.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.Unit;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewRecipeActivity extends AppCompatActivity {

    LinearLayout ll_ingredientsbutton, ll_descriptionbutton, ll_share, ll_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        Recipe selectedRecipe = recipeViewModel.getCurrentRecipeById(intent.getIntExtra("id", 404040));

        //Testdaten
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Zucker", 100, Unit.Unit));
        ingredients.add(new Ingredient("Alkohol", 50, Unit.Liter));
        selectedRecipe = new Recipe("Mojito", "Schmeckt gut mit Alkohol lol", ingredients);
        //Testdaten

        getSupportActionBar().setTitle(selectedRecipe.getName());


        ll_descriptionbutton = findViewById(R.id.ll_view_recipe_descriptionbutton);
        ll_edit = findViewById(R.id.ll_view_recipe_editbutton);
        ll_ingredientsbutton = findViewById(R.id.ll_view_recipe_ingredientbutton);
        ll_share = findViewById(R.id.ll_view_recipe_sharebutton);

        ll_ingredientsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ll_descriptionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}
