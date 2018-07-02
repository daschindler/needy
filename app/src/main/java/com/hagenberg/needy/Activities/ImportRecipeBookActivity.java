package com.hagenberg.needy.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;
import com.hagenberg.needy.Entity.Unit;
import com.hagenberg.needy.MainActivity;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeBookViewModel;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ImportRecipeBookActivity extends AppCompatActivity {

    private LinearLayout llFiles;
    private Button btUp;
    private File actPathFile;
    private int hierarchyCounter = 0;
    public boolean inserted = false;
    public List<Recipe> initialRecipes = new LinkedList<Recipe>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_recipe_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        llFiles = findViewById(R.id.activity_importRB_ll_files);
        btUp = findViewById(R.id.activity_importRB_bt_up);
        btUp.setVisibility(View.GONE);

        final File sdcard = Environment.getExternalStorageDirectory();

        setupFileBrowser(sdcard);

        btUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hierarchyCounter--;
                if(hierarchyCounter==0) {
                    btUp.setVisibility(View.GONE);
                }
                llFiles.removeAllViews();
                setupFileBrowser(actPathFile.getParentFile());
            }
        });
    }

    //Unbuilds the imported recipebook file, by unbuilding its string, and splitting the lines by the built in seperators from before.
    private void StoreRecipeBook(File file) {
        StringBuilder unbuildString = new StringBuilder();

        try{
            BufferedReader br = new BufferedReader(new FileReader((file)));
            String line;

            while((line = br.readLine()) != null) {
                unbuildString.append(line);
            }

            br.close();
        } catch (IOException e) {
            Log.e("readRecipeBookFromFile", e.getMessage());
        }

        String[] recipeBookArray = unbuildString.toString().split(";");

        String recipeBookName = recipeBookArray[0];
        String recipeBookDescription = recipeBookArray[1];
        final List<Recipe> recipes = new ArrayList<>();

        //Split recipes.
        for(int i = 2; i < recipeBookArray.length; i++) {
            String[] recipeArray = recipeBookArray[i].split(":");
            String recipeName = recipeArray[0];
            String recipeDesc = recipeArray[1];
            List<Ingredient> ingredients = new ArrayList<>();

            //Split ingredients for actual recipe.
            for(int y = 2; y < recipeArray.length; y++) {
                String[] ingredientArray = recipeArray[y].split("/");
                try {
                    String ingredientName = ingredientArray[0];
                    double ingredientAmount = Double.valueOf(ingredientArray[1]);
                    Unit ingredientUnit = Unit.valueOf(ingredientArray[2]);
                    ingredients.add(new Ingredient(ingredientName, ingredientAmount, ingredientUnit));
                } catch (Exception ex) {
                    //in here ==> no ingredients for this recipe
                    y = recipeArray.length;
                }

            }
            recipes.add(new Recipe(recipeName, recipeDesc, ingredients));
        }

        final RecipeBook importedBook = new RecipeBook();
        importedBook.setName(recipeBookName);
        importedBook.setDescription(recipeBookDescription);
        final RecipeViewModel rViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        final RecipeBookViewModel rbViewModel = ViewModelProviders.of(this).get(RecipeBookViewModel.class);

        //Observe all recipes from the database.
        rViewModel.getAllRecipes().getValue();
        rViewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable final List<Recipe> startRecipes) {
                if(startRecipes!=null){
                    if(!inserted) {
                        //Insert Recipes from the imported book into db.
                        initialRecipes = startRecipes;
                        for (Recipe insertRec : recipes) {
                            rViewModel.insert(insertRec);
                        }
                        inserted = true;
                    } else {
                        //Recipes are done inserting, recipebook is inserted now with its previous inserted recipes.
                        if(startRecipes.size()==initialRecipes.size()+recipes.size()) {
                            List<Recipe> newRecipes = filterRecipes(initialRecipes, startRecipes);
                            importedBook.setRecipies(newRecipes);
                            rbViewModel.insert(importedBook);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    //Returns the newly inserted Recipes, that weren't in the list before importing the recipe book.
    private List<Recipe> filterRecipes(List<Recipe> startRecipes, List<Recipe> endRecipes) {
        List<Recipe> newRecipes = new LinkedList<Recipe>();

        for(Recipe rec : endRecipes) {
            if(!isRecipeInList(rec, startRecipes)) {
                newRecipes.add(rec);
            }
        }

        return newRecipes;
    }

    //Returns true if the parameter recipe is contained in the list of recipes.
    private boolean isRecipeInList(Recipe recipe, List<Recipe> startRecipes) {
        for(Recipe rec : startRecipes) {
            if(rec.getUid()==recipe.getUid()) {
                return true;
            }
        }
        return false;
    }

    //Sets up the layout of a file browser, which only shows ".rbneedy"-files. Also sets up a "Level Up"-Button.
    private void setupFileBrowser(File topfile) {
        actPathFile = topfile;
        File directories = new File(topfile.getAbsolutePath());

        if(directories.exists()) {
            final File[] files = directories.listFiles();
            for (int i = 0; i < files.length; i++) {
                final LinearLayout llProgExplorer = new LinearLayout(this);
                llProgExplorer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 160));
                llProgExplorer.setPadding(70, 60,100,10);
                llProgExplorer.setOrientation(LinearLayout.HORIZONTAL);
                ImageView imgvType = new ImageView(this);

                if(files[i].isDirectory()) {
                    if(!files[i].getName().startsWith(".") &&
                            !files[i].getName().contains(".")) {
                        TextView tvFolderName = new TextView(this);
                        tvFolderName.setText(files[i].getName());
                        tvFolderName.setId(i + 1);
                        tvFolderName.setTextSize(21);
                        imgvType.setImageResource(R.drawable.ic_folder_grey_24dp);
                        imgvType.setId(i + 2);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        params.weight = 1.0f;
                        tvFolderName.setLayoutParams(params);
                        llProgExplorer.setId(i);
                        llProgExplorer.addView(tvFolderName);
                        llProgExplorer.addView(imgvType);
                        llFiles.addView(llProgExplorer);

                        llProgExplorer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(hierarchyCounter == 0) {
                                    btUp.setVisibility(View.VISIBLE);
                                }
                                hierarchyCounter++;
                                int id = llProgExplorer.getId();
                                llFiles.removeAllViews();;
                                setupFileBrowser(files[id]);
                            }
                        });
                    }
                } else if (files[i].isFile()) {
                    if(files[i].getName().contains(".rbneedy") && !files[i].getName().startsWith(".")) {
                        TextView tvFileName = new TextView(this);
                        tvFileName.setText(files[i].getName());
                        tvFileName.setId(i + 1);
                        tvFileName.setTextSize(21);
                        tvFileName.setTextColor(getColor(R.color.colorPrimary));

                        llProgExplorer.setId(i);

                        imgvType.setImageResource(R.drawable.ic_insert_drive_file_grey_24dp);
                        imgvType.setId(i+2);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        params.weight = 1.0f;
                        tvFileName.setLayoutParams(params);

                        llProgExplorer.addView(tvFileName);
                        llProgExplorer.addView(imgvType);
                        llFiles.addView(llProgExplorer);

                        llProgExplorer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                StoreRecipeBook(files[llProgExplorer.getId()]);
                            }
                        });
                    }
                }
            }
        }
    }
}
