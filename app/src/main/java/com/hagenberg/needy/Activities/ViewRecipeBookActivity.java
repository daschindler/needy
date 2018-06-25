package com.hagenberg.needy.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Adapters.ViewRecipeBookListAdapter;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeBookViewModel;

import java.util.LinkedList;

public class ViewRecipeBookActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvDesc;
    RecyclerView rvRecipes;
    RecipeBookViewModel recipeBookViewModel;
    RecyclerView.LayoutManager layoutManager;
    ViewRecipeBookListAdapter listAdapter = new ViewRecipeBookListAdapter(new LinkedList<Recipe>());
    RecipeBook rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_book);
        tvName = findViewById(R.id.viewRB_tv_name);
        tvDesc = findViewById(R.id.viewRB_tv_desc);
        rvRecipes = findViewById(R.id.viewRB_rv_recipes);
        recipeBookViewModel = ViewModelProviders.of(this).get(RecipeBookViewModel.class);

        layoutManager = new LinearLayoutManager(this);
        rvRecipes.setLayoutManager(layoutManager);

        int id = 0;
        Intent calling = getIntent();
        Bundle extras = calling.getExtras();

        if(extras != null) {
            id = (int) extras.get("id");
        }
        rb = recipeBookViewModel.getCurrentRecipeBookById(id);
        if(rb!= null) {
            tvName.setText(rb.getName());
            tvDesc.setText(rb.getDescription());
            listAdapter = new ViewRecipeBookListAdapter(rb.getRecipies());
            rvRecipes.setAdapter(listAdapter);
        }

        //Next step: Set up menu. 1.5 Hours in. 0945 started again.


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_recipe_book_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.view_recipe_book_menu_delete:
                Toast.makeText(this, "Delete has been clicked.", Toast.LENGTH_SHORT).show();
                deleteRecipeBook();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteRecipeBook() {
        if(rb!=null) {
            //Delete RB.
        }
        onBackPressed();
    }
}
