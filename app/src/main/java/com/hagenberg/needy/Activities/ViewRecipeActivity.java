package com.hagenberg.needy.Activities;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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

    private LinearLayout ll_ingredientsbutton, ll_descriptionbutton;
    private LinearLayout ll_ingredients, ll_description;

    private ImageView imgv_ingredientsarrow, imgv_descriptionarrow;

    private TextView tv_description_label, tv_ingredient_label;

    private Button bt_share, bt_edit;

    private boolean ingredientsShown = false;
    private boolean descriptionShown = false;

    private Recipe selectedRecipe;

    private ColorStateList originalLabelColors;

    private int recipeId = 404040;

    RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        ll_descriptionbutton = findViewById(R.id.ll_view_recipe_descriptionbutton);
        ll_ingredientsbutton = findViewById(R.id.ll_view_recipe_ingredientbutton);
        ll_description = findViewById(R.id.ll_view_recipe_description);
        ll_ingredients = findViewById(R.id.ll_view_recipe_ingredients);
        imgv_descriptionarrow = findViewById(R.id.img_view_recipe_description_arrow);
        imgv_ingredientsarrow = findViewById(R.id.img_view_recipe_ingredients_arrow);
        tv_description_label = findViewById(R.id.tv_view_recipe_description_label);
        tv_ingredient_label = findViewById(R.id.tv_view_recipe_ingredient_label);
        bt_share = findViewById(R.id.bt_view_recipe_share);
        bt_edit = findViewById(R.id.bt_view_recipe_edit);

        final int displayWidth = getDisplayWidth();

        recipeId = intent.getIntExtra("id", 404040);

        if (recipeId != 404040) {
            final LiveData<Recipe> selectedLiveRecipe = recipeViewModel.getRecipeById(recipeId);
            selectedLiveRecipe.observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable final Recipe recipe) {
                    if(recipe != null){
                        selectedRecipe = recipe;
                        getSupportActionBar().setTitle(selectedRecipe.getName());
                        HideIngredients();
                        ShowIngredients(displayWidth);
                        HideDescription();
                        ShowDescription();
                    }
                }
            });

            originalLabelColors = tv_description_label.getTextColors();


            ll_ingredientsbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ingredientsShown == false) {
                        ShowIngredients(displayWidth);
                    } else {
                        HideIngredients();
                    }
                }
            });

            ll_descriptionbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (descriptionShown == false) {
                        ShowDescription();
                    } else {
                        HideDescription();
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

    /**
     * @return
     * The displaywidth of the device where it is executed.
     */
    private int getDisplayWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    /**
     * This method hides the description in the view
     */
    private void HideDescription() {
        ll_description.removeAllViews();
        descriptionShown = false;
        imgv_descriptionarrow.setImageResource(R.drawable.ic_expand_more_grey_24dp);
        tv_description_label.setTextColor(originalLabelColors);
    }

    /**
     * This method shows the view where the description of the
     * current recipe is displayed
     */
    private void ShowDescription() {
        TextView tv_description = new TextView(ViewRecipeActivity.this);
        tv_description.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv_description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv_description.setTextSize(17);
        tv_description.setPadding(110,0,100,40);

        tv_description.setText(selectedRecipe.getDescription() + "\n");

        imgv_descriptionarrow.setImageResource(R.drawable.ic_expand_less_black_24dp);
        tv_description_label.setTextColor(Color.rgb(0,0,0));
        descriptionShown = true;
        ll_description.addView(tv_description);
    }

    /**
     * Hides the ingredients view
     */
    private void HideIngredients() {
        ll_ingredients.removeAllViews();
        ll_ingredients.setPadding(0,0,0,0);
        ingredientsShown = false;
        imgv_ingredientsarrow.setImageResource(R.drawable.ic_expand_more_grey_24dp);
        tv_ingredient_label.setTextColor(originalLabelColors);
    }

    /**
     * Show all ingredients that are needed for the current recipe
     * @param displayWidth
     * Is needed for a good looking layout on every phone
     */
    private void ShowIngredients(int displayWidth) {
        if (selectedRecipe.getIngredients() != null) {
            for (Ingredient loopIngredient : selectedRecipe.getIngredients()){
                ll_ingredients.setPadding(0,0,0,60);

                LinearLayout horizontalTextLayout = new LinearLayout(this);
                LinearLayout.LayoutParams horizontalTextLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                horizontalTextLayout.setOrientation(LinearLayout.HORIZONTAL);
                horizontalTextLayout.setLayoutParams(horizontalTextLayoutParams);
                horizontalTextLayout.setPadding((displayWidth/12),10,displayWidth/12,10);

                TextView tvIngredientAmount = new TextView(this);
                LinearLayout.LayoutParams tvIngredientAmountParams =
                        new LinearLayout.LayoutParams((160), ViewGroup.LayoutParams.WRAP_CONTENT);
                tvIngredientAmount.setText(String.valueOf(loopIngredient.getAmount()));
                tvIngredientAmount.setTextColor(originalLabelColors);
                tvIngredientAmount.setTextSize(17);
                tvIngredientAmount.setLayoutParams(tvIngredientAmountParams);

                View fillerViewAmountUnit = new View(this);
                fillerViewAmountUnit.setLayoutParams(new LinearLayout.LayoutParams(displayWidth/30, ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView tvIngredientUnit = new TextView(this);
                LinearLayout.LayoutParams tvIngredientUnitParams =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tvIngredientUnit.setText(loopIngredient.getAmountUnit().toString());
                tvIngredientUnit.setTextColor(originalLabelColors);
                tvIngredientUnit.setTextSize(17);
                tvIngredientUnit.setLayoutParams(tvIngredientUnitParams);

                TextView tvIngredientName = new TextView(this);
                LinearLayout.LayoutParams tvIngredientNameParams =
                        new LinearLayout.LayoutParams((displayWidth/3), ViewGroup.LayoutParams.WRAP_CONTENT);
                tvIngredientName.setText(loopIngredient.getName().toString());
                tvIngredientName.setTextColor(originalLabelColors);
                tvIngredientName.setTextSize(17);
                tvIngredientName.setLayoutParams(tvIngredientNameParams);

                horizontalTextLayout.addView(tvIngredientName);
                horizontalTextLayout.addView(fillerViewAmountUnit);
                horizontalTextLayout.addView(tvIngredientAmount);
                horizontalTextLayout.addView(tvIngredientUnit);

                ll_ingredients.addView(horizontalTextLayout);
            }
        }

        imgv_ingredientsarrow.setImageResource(R.drawable.ic_expand_less_black_24dp);
        tv_ingredient_label.setTextColor(Color.rgb(0,0,0));
        ingredientsShown = true;
    }


    /**
     * is called when user presses on share
     * the recipe is stored as .needy file on the sdcard root /needy folder
     * folder will be generated if not already there
     */
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

    /**
     * Makes a .needy formatted String out of an object that is later stored in the .needy file
     * @param recipe
     * @return
     */
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
            formattedRecipe.append("no ingredientname//;");
        }

        return formattedRecipe.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.view_recipe_menu_delete:
                recipeViewModel.delete(selectedRecipe);
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            default:
                this.finish();
                break;
        }
        return true;
    }
}
