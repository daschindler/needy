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

/**
 * ActivityClass for creating a Recipe Book.
 */
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
    Boolean update = false; //Activity used for both update and create, if update == true ==> RecipeBook will be updated, not inserted.


    /**
     * onCreate method called when Activity is created. Initializes Views and adapters for display.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Init views
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
                        recipeBookViewModel.update(recipeBookNew);
                        onBackPressed();
                    }
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

    /**
     * Checks if all the views are filled, and shakes the views that aren't. If User got everything correct, his recipebook is returned by this method.
     * @return a RecipeBook with the user given values, if all is filled out, or otherwise an empty recipebook.
     */
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

        //User filled out everything, recipebook is created now
        recipeBook.setRecipies(checkedRecipes);
        recipeBook.setName(etRecipeBookName.getText().toString());
        recipeBook.setDescription(etRecipeBookDescription.getText().toString());
        return recipeBook;
    }

    /**
     * Initializes the RecyclerView, creates observer for recyclerviews data. Two different branches in this method, on for updating and one for usual inserting.
     */
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

    /**
     * Returns a boolean array with true set for the recipes in the database, that are also in the recipebook which is updated in this activity.
     * @param allRec list of all recipes available in the db
     * @param rbRec list of the recipes related to recipebooks.
     * @return returns an array as big as allRec-List, which holds true if the value on this position in the allRec is also in the rbRec, and false if otherwise.
     */
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

    /**
     * Checks if a recipe is contained in a list of recipes, and returns true if so.
     * @param rbRec List of recipes
     * @param recipe Recipe
     * @return Returns true if recipe is in rbRec, and false otherwise.
     */
    private boolean recipeBookIncluded(List<Recipe> rbRec, Recipe recipe) {
        for(Recipe rec : rbRec) {
            if(rec.getUid()==recipe.getUid()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Called on creation, to create and inflate the options menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!update) {   //Displays the import button in the action bar only when a recipebook is created, not updated.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_recipe_book_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Callback method for when a user clicks an item in the options menu. Handles the clicks on those items.
     * @param item An item in the menu.
     * @return Handled by superclass.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.createRB_menu_import:
                if(checkStoragePermission()) {
                    //If user gave permission to access storage, this method starts the import-Intent.
                    Intent intent = new Intent(this, ImportRecipeBookActivity.class);
                    startActivity(intent);
                } else {
                    askStoragePermission();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Callback for Permission requests. User was asked about sharing his storage with this application, the answer is handled in this callback-method.
     * @param requestCode code with which the permission-question was submitted to the user.
     * @param permissions permissions we asked for
     * @param grantResults results for the asked permissions.
     */
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

    /**
     * Checks if a user has given this app the permission to write the storage.
     * @return true if permission already granted, false if not.
     */
    private boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    /**
     * Asks the user for permission to write to his storage.
     */
    private void askStoragePermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
    }
}
