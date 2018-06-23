package com.hagenberg.needy.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hagenberg.needy.Adapters.CreateRecipeBookListAdapter;
import com.hagenberg.needy.Adapters.ShowAllRecipesListAdapter;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeBookViewModel;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateRecipeBookActivity extends AppCompatActivity {

    Button btFinish;
    EditText etRecipeBookName;
    RecyclerView rvChoosenRecipes;
    RecyclerView.LayoutManager layoutManager;
    RecipeViewModel recipeViewModel;
    CreateRecipeBookListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe_book);

        btFinish = (Button) findViewById(R.id.createRB_bt_finish);
        etRecipeBookName = (EditText) findViewById(R.id.createRB_et_name);
        rvChoosenRecipes = (RecyclerView) findViewById(R.id.createRB_rv_recipes);

        //RecyclerView Specs + Adapter + Values setzen
        layoutManager = new LinearLayoutManager(this);
        rvChoosenRecipes.setLayoutManager(layoutManager);

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeBook recipeBookNew = checkViews();
                if(recipeBookNew!= null) {
                    //insert
                    Toast.makeText(getBaseContext(), "Insert would have been possible!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Some recipe values for the adapter, usually taken outta the SQLite Database
        initializeListAdapter();
    }

    private RecipeBook checkViews() {
        RecipeBook recipeBook = new RecipeBook();

        if(etRecipeBookName.getText().toString().length()==0)
        {
            Toast.makeText(this.getBaseContext(), "Please choose a proper name for your Recipe Book!", Toast.LENGTH_SHORT).show();
            etRecipeBookName.setHighlightColor(getColor(R.color.colorPrimaryDark));
            return null;
        }
        List<Recipe> checkedRecipes = listAdapter.getCheckedRecipes();
        if(checkedRecipes.size()==0) {
            Toast.makeText(this.getBaseContext(), "Please choose at least one recipe to add to your book!", Toast.LENGTH_SHORT).show();
            return null;
        }
        recipeBook.setRecipies(checkedRecipes);
        recipeBook.setName(etRecipeBookName.getText().toString());
        return recipeBook;
    }

    private void initializeListAdapter() {
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
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
