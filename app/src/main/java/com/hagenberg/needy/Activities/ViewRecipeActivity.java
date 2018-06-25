package com.hagenberg.needy.Activities;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    private Button bt_share;

    private boolean ingredientsShown = false;
    private boolean descriptionShown = false;

    private ColorStateList originalLabelColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        //final Recipe selectedRecipe = recipeViewModel.getCurrentRecipeById(intent.getIntExtra("id", 404040));

        //Testdaten

        final List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Zucker", 100, Unit.Unit));
        ingredients.add(new Ingredient("Alkohol", 50, Unit.Liter));
        final Recipe selectedRecipe = new Recipe("Mojito", "Schmeckt gut mit Alkohol lol, und das ist " +
                "ein Test um die Funktionalität des Programms zu testen und zu sehen wie alles so ist", ingredients);
        //Testdaten

        getSupportActionBar().setTitle(selectedRecipe.getName());


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

        originalLabelColors = tv_description_label.getTextColors();

        ll_ingredientsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingredientsShown == false) {
                    TextView tv_ingredients = new TextView(ViewRecipeActivity.this);
                    tv_ingredients.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv_ingredients.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    StringBuilder ingredientString = new StringBuilder();
                    for (Ingredient ingredient : selectedRecipe.getIngredients()) {
                        ingredientString.append(ingredient.getAmount() + " " + ingredient.getAmountUnit().toString() + " " + ingredient.getName() + "\n");
                    }

                    tv_ingredients.setText(ingredientString.toString());

                    ll_ingredients.addView(tv_ingredients);
                    imgv_ingredientsarrow.setImageResource(R.drawable.ic_expand_less_black_24dp);
                    tv_ingredient_label.setTextColor(Color.rgb(0,0,0));
                    ingredientsShown = true;
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
                try {
                    File folder = new File(Environment.getExternalStorageDirectory().toString()+"/needy");
                    if (!folder.isDirectory()) {
                        folder.mkdirs();
                    }

                    File file = new File(Environment.getExternalStorageDirectory().toString(), "/needy/"+selectedRecipe.getName()+".txt");
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutput);
                    outputStreamWriter.write(selectedRecipe.getName() + ";" + selectedRecipe.getDescription());
                    outputStreamWriter.flush();
                    fileOutput.getFD().sync();
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.d("Write to storage", "Failed");
                }

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory().toString() +
                        "/needy/"+selectedRecipe.getName()+".txt"));
                startActivity(Intent.createChooser(sharingIntent, "share file with"));
            }
        });

        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}
