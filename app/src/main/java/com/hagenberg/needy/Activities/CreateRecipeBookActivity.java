package com.hagenberg.needy.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hagenberg.needy.Adapters.CreateRecipeBookListAdapter;
import com.hagenberg.needy.Adapters.ShowAllRecipesListAdapter;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;
import com.hagenberg.needy.MainActivity;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeBookViewModel;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateRecipeBookActivity extends AppCompatActivity {

    Button btFinish;
    EditText etRecipeBookName;
    EditText etRecipeBookDescription;
    RecyclerView rvChoosenRecipes;
    RecyclerView.LayoutManager layoutManager;
    RecipeViewModel recipeViewModel;
    RecipeBookViewModel recipeBookViewModel;
    CreateRecipeBookListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btFinish = findViewById(R.id.createRB_bt_finish);
        etRecipeBookName = findViewById(R.id.createRB_et_name);
        rvChoosenRecipes = findViewById(R.id.createRB_rv_recipes);
        etRecipeBookDescription = findViewById(R.id.createRB_et_desc);

        //RecyclerView Specs + Adapter + Values setzen
        layoutManager = new LinearLayoutManager(this);
        rvChoosenRecipes.setLayoutManager(layoutManager);

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeBook recipeBookNew = checkViews();
                if(recipeBookNew!= null) {
                    //insert
                    recipeBookViewModel.insert(recipeBookNew);
                    onBackPressed();    //Go back to main activity.
                }
            }
        });

        //Some recipe values for the adapter, usually taken outta the SQLite Database
        initializeListAdapter();
    }

    private RecipeBook checkViews() {
        RecipeBook recipeBook = new RecipeBook();
        boolean correctInput = true;
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        if(etRecipeBookName.getText().toString().length()==0)
        {
            etRecipeBookName.startAnimation(shake);
            correctInput = false;
        }
        if(etRecipeBookDescription.getText().toString().length()==0){
            etRecipeBookDescription.startAnimation(shake);
            correctInput = false;
        }
        List<Recipe> checkedRecipes = listAdapter.getCheckedRecipes();
        if(checkedRecipes.size()==0) {
            Toast.makeText(this.getBaseContext(), "Please choose at least one recipe to add to your book!", Toast.LENGTH_SHORT).show();
            correctInput = false;
        }
        if(!correctInput) {
            return null;
        }
        recipeBook.setRecipies(checkedRecipes);
        recipeBook.setName(etRecipeBookName.getText().toString());
        recipeBook.setDescription(etRecipeBookDescription.getText().toString());
        return recipeBook;
    }

    private void initializeListAdapter() {
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        recipeBookViewModel = ViewModelProviders.of(this).get(RecipeBookViewModel.class);
        LiveData<List<Recipe>> allRecipes = recipeViewModel.getAllLiveRecipes();
        List<Recipe> recipeList = allRecipes.getValue();

        if(recipeList == null) {
            recipeList = new LinkedList<Recipe>();
        }
        listAdapter = new CreateRecipeBookListAdapter(recipeList);

        allRecipes.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes != null) {
                    listAdapter.updateData(recipes);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

        rvChoosenRecipes.setAdapter(listAdapter);
    }

}
