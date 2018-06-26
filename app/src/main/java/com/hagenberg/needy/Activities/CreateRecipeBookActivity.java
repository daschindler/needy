package com.hagenberg.needy.Activities;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    List<Recipe> checkedRecipes;
    List<Recipe> allDatabaseRecipes;
    int id;
    Boolean update = false;


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
                    if(update) {
                        //recipeBookViewModel.update(recipeBookNew);
                        onBackPressed();
                    }
                    //insert
                    else {
                        recipeBookViewModel.insert(recipeBookNew);
                        onBackPressed();    //Go back to main activity.
                    }
                }
            }
        });

        //In case this activity is called with edit-purposes...
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getInt("id");
            update = true;
            btFinish.setText("Save Changes");
        }

        initializeListAdapter();
    }

    private RecipeBook checkViews() {
        RecipeBook recipeBook = new RecipeBook();
        if(update) {
            recipeBook.setUid(this.id);
        }
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

        if(update) {
            LiveData<RecipeBook> recipeBook = recipeBookViewModel.getRecipeBookById(id);
            recipeBook.observe(this, new Observer<RecipeBook>() {
                @Override
                public void onChanged(@Nullable RecipeBook recipeBook) {
                    if(recipeBook!=null) {
                        //Initialize Update views
                        etRecipeBookName.setText(recipeBook.getName());
                        etRecipeBookDescription.setText(recipeBook.getDescription());
                        checkedRecipes = recipeBook.getRecipies();

                        if(checkedRecipes!=null && allDatabaseRecipes!=null) {
                            Boolean[] checked = updateRecipeView(allDatabaseRecipes, checkedRecipes);
                            listAdapter.updateData(allDatabaseRecipes, checked);
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

        final LiveData<List<Recipe>> allRecipes = recipeViewModel.getAllRecipes();
        List<Recipe> recipeList = allRecipes.getValue();

        if(recipeList == null) {
            recipeList = new LinkedList<Recipe>();
        }
        listAdapter = new CreateRecipeBookListAdapter(recipeList);

        allRecipes.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes != null) {
                    allDatabaseRecipes = recipes;

                    if(allDatabaseRecipes!=null && checkedRecipes!=null) {
                        Boolean[] checked = updateRecipeView(allDatabaseRecipes, checkedRecipes);
                        listAdapter.updateData(allDatabaseRecipes, checked);
                    } else {
                        listAdapter.updateData(recipes);
                    }
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

        rvChoosenRecipes.setAdapter(listAdapter);
    }

    private Boolean[] updateRecipeView(List<Recipe> allRec, List<Recipe> rbRec) {
        Boolean[] checked = new Boolean[allRec.size()];

        for(int i = 0; i < allRec.size(); i++) {
            if(recipeBookIncluded(rbRec, allRec.get(i))) {
                checked[i] = true;
            } else {
                checked[i] = false;
            }
        }

        return checked;
    }

    private boolean recipeBookIncluded(List<Recipe> rbRec, Recipe recipe) {
        for(Recipe rec : rbRec) {
            if(rec.getUid()==recipe.getUid()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_recipe_book_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.createRB_menu_import:
                if(checkStoragePermission()) {
                    Intent intent = new Intent(this, ImportRecipeBookActivity.class);
                    startActivity(intent);
                } else {
                    askStoragePermission();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
                    Intent intent = new Intent(this, ImportRecipeBookActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "We are sorry, but importing Recipe Books is only possible with storage access...", Toast.LENGTH_LONG).show();
                }
            default:
                return;
        }
    }

    private boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void askStoragePermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
    }
}
