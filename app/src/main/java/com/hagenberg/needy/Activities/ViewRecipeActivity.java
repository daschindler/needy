package com.hagenberg.needy.Activities;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.Unit;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ViewRecipeActivity extends AppCompatActivity {

    private LinearLayout ll_ingredientsbutton, ll_descriptionbutton, ll_share, ll_edit;
    private LinearLayout ll_ingredients, ll_description;

    private ImageView imgv_ingredientsarrow, imgv_descriptionarrow;

    private TextView tv_description_label, tv_ingredient_label;

    private Button bt_share, bt_edit;

    private boolean ingredientsShown = false;
    private boolean descriptionShown = false;

    private Recipe selectedRecipe;

    private ColorStateList originalLabelColors;

    private int recipeId = 404040;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        ll_descriptionbutton = findViewById(R.id.ll_view_recipe_descriptionbutton);
        ll_edit = findViewById(R.id.ll_view_recipe_editbutton);
        ll_ingredientsbutton = findViewById(R.id.ll_view_recipe_ingredientbutton);
        ll_description = findViewById(R.id.ll_view_recipe_description);
        ll_ingredients = findViewById(R.id.ll_view_recipe_ingredients);
        imgv_descriptionarrow = findViewById(R.id.img_view_recipe_description_arrow);
        imgv_ingredientsarrow = findViewById(R.id.img_view_recipe_ingredients_arrow);
        tv_description_label = findViewById(R.id.tv_view_recipe_description_label);
        tv_ingredient_label = findViewById(R.id.tv_view_recipe_ingredient_label);
        bt_share = findViewById(R.id.bt_view_recipe_share);
        bt_edit = findViewById(R.id.bt_view_recipe_edit);


        recipeId = intent.getIntExtra("id", 404040);

        if (recipeId != 404040) {
            final LiveData<Recipe> selectedLiveRecipe = recipeViewModel.getRecipeById(recipeId);


            selectedLiveRecipe.observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable final Recipe recipe) {
                    if(recipe != null){
                        selectedRecipe = recipe;
                        getSupportActionBar().setTitle(selectedRecipe.getName());
                        ShowIngredients();
                    }
                }
            });

            originalLabelColors = tv_description_label.getTextColors();


            ll_ingredientsbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ingredientsShown == false) {
                        ShowIngredients();
                    } else {
                        ll_ingredients.removeAllViews();
                        ingredientsShown = false;
                        imgv_ingredientsarrow.setImageResource(R.drawable.ic_expand_more_grey_24dp);
                        tv_ingredient_label.setTextColor(originalLabelColors);
                    }
                }
            });

            ll_descriptionbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (descriptionShown == false) {
                        TextView tv_description = new TextView(ViewRecipeActivity.this);
                        tv_description.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tv_description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv_description.setPadding(40,0,40,0);

                        tv_description.setText(selectedRecipe.getDescription() + "\n");

                        imgv_descriptionarrow.setImageResource(R.drawable.ic_expand_less_black_24dp);
                        tv_description_label.setTextColor(Color.rgb(0,0,0));
                        descriptionShown = true;
                        ll_description.addView(tv_description);
                    } else {
                        ll_description.removeAllViews();
                        descriptionShown = false;
                        imgv_descriptionarrow.setImageResource(R.drawable.ic_expand_more_grey_24dp);
                        tv_description_label.setTextColor(originalLabelColors);
                    }
                }
            });

            bt_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateFileAndShare();
                }
            });

            bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentToEdit = new Intent(ViewRecipeActivity.this, EditRecipeActivity.class);
                    intentToEdit.putExtra("id", recipeId);
                    ViewRecipeActivity.this.startActivity(intentToEdit);
                }
            });
        } else {
            Toast.makeText(this, "Error loading recipe :(", Toast.LENGTH_SHORT).show();
        }




    }

    private void ShowIngredients() {
        TextView tv_ingredients = new TextView(ViewRecipeActivity.this);
        tv_ingredients.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv_ingredients.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        StringBuilder ingredientString = new StringBuilder();
        if (selectedRecipe.getIngredients() != null) {
            for (Ingredient ingredient : selectedRecipe.getIngredients()) {
                ingredientString.append(ingredient.getAmount() + " " + ingredient.getAmountUnit().toString() + " " + ingredient.getName() + "\n");
            }
        } else {
            ingredientString.append("No ingredients added\n");
        }


        tv_ingredients.setText(ingredientString.toString());

        ll_ingredients.addView(tv_ingredients);
        imgv_ingredientsarrow.setImageResource(R.drawable.ic_expand_less_black_24dp);
        tv_ingredient_label.setTextColor(Color.rgb(0,0,0));
        ingredientsShown = true;
    }


    private void CreateFileAndShare() {
        try {
            File folder = new File(Environment.getExternalStorageDirectory().toString()+"/needy");
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }

            File file = new File(Environment.getExternalStorageDirectory().toString(), "/needy/"+selectedRecipe.getName()+".needy");
            FileOutputStream fileOutput = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutput);
            String fileContent = FormatRecipeToString(selectedRecipe);
            outputStreamWriter.write(fileContent);
            outputStreamWriter.flush();
            fileOutput.getFD().sync();
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.d("Write to storage", "Failed");
        }

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory().toString() +
                "/needy/"+selectedRecipe.getName()+".needy"));
        startActivity(Intent.createChooser(sharingIntent, "share file with"));
    }

    private String FormatRecipeToString(Recipe recipe) {
        StringBuilder formattedRecipe = new StringBuilder();

        if (recipe.getName() != null){
            formattedRecipe.append(recipe.getName()+";");
        } else {
            formattedRecipe.append("no name;");
        }

        if (recipe.getDescription() != null){
            formattedRecipe.append(recipe.getDescription()+";");
        } else {
            formattedRecipe.append("no description;");
        }

        if (recipe.getIngredients() != null) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                formattedRecipe.append(ingredient.getName()+"/");
                formattedRecipe.append(ingredient.getAmount()+"/");
                formattedRecipe.append(ingredient.getAmountUnit().toString()+";");
            }
        } else {
            formattedRecipe.append("no ingredientname/no ingredientdesc/no unit;");
        }

        return formattedRecipe.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            default:
                this.finish();
                break;
        }
        return true;
    }
}
