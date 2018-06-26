package com.hagenberg.needy.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.Unit;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.ArrayList;

public class EditRecipeActivity extends AppCompatActivity {

    private int recipeId = 404040;

    private Recipe selectedRecipe;

    private LinearLayout llDescriptionButton, llIngredientButton, llIngredients, llDescription;

    private Button btSaveEditedRecipe;

    private EditText etRecipeName;

    private TextView tvIngredientLabel, tvDescriptionLabel;
    private ImageView imgvIngredientSymbol, imgvDescriptionSymbol;

    private boolean ingredientsShown = false;
    private boolean descriptionShown = false;

    private ColorStateList colorGrey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        recipeId = intent.getIntExtra("id", 404040);

        llDescription = findViewById(R.id.ll_edit_recipe_description);
        llDescriptionButton = findViewById(R.id.ll_edit_recipe_descriptionbutton);
        llIngredientButton = findViewById(R.id.ll_edit_recipe_ingredientbutton);
        llIngredients = findViewById(R.id.ll_edit_recipe_ingredients);
        btSaveEditedRecipe = findViewById(R.id.bt_edit_recipe_save);
        etRecipeName = findViewById(R.id.et_edit_recipe_recipe_name);
        tvIngredientLabel = findViewById(R.id.tv_edit_recipe_ingredient_label);
        tvDescriptionLabel = findViewById(R.id.tv_edit_recipe_description_label);
        imgvDescriptionSymbol = findViewById(R.id.img_edit_recipe_description_arrow);
        imgvIngredientSymbol = findViewById(R.id.img_edit_recipe_ingredients_arrow);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int displayWidth = size.x;

        llDescription.setPadding(displayWidth/8,0,displayWidth/8,55);
        llIngredients.setPadding(0,0,0,35);
        llDescription.setVisibility(View.GONE);
        llIngredients.setVisibility(View.GONE);

        colorGrey = tvIngredientLabel.getTextColors();


        //Hide SoftKeyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (recipeId != 404040) {
            final LiveData<Recipe> selectedLiveRecipe = recipeViewModel.getRecipeById(recipeId);
            selectedLiveRecipe.observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable final Recipe recipe) {
                    if(recipe != null){
                        selectedRecipe = recipe;
                        getSupportActionBar().setTitle("Edit " + selectedRecipe.getName());
                        etRecipeName.setText(selectedRecipe.getName());
                        ShowEditIngredients(displayWidth);
                    }
                }
            });

            etRecipeName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        getSupportActionBar().setTitle("Edit " + charSequence);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            llDescriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (descriptionShown == false) {
                        ShowEditDescription(displayWidth);
                    } else {
                        HideEditDescription();
                    }
                }
            });

            llIngredientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ingredientsShown == false) {
                        ShowEditIngredients(displayWidth);
                    } else {
                        HideEditIngredients();
                    }

                }
            });

            btSaveEditedRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditRecipeActivity.this.finish();
                }
            });

        } else {
            Toast.makeText(this, "Error loading Recipe :(", Toast.LENGTH_SHORT).show();
        }

    }

    private void HideEditDescription() {
        llDescription.removeAllViews();
        descriptionShown = false;
        imgvDescriptionSymbol.setImageResource(R.drawable.ic_expand_more_grey_24dp);
        tvDescriptionLabel.setTextColor(colorGrey);
        llDescription.setVisibility(View.GONE);
    }

    private void HideEditIngredients() {
        llIngredients.removeAllViews();
        ingredientsShown = false;
        imgvIngredientSymbol.setImageResource(R.drawable.ic_expand_more_grey_24dp);
        tvIngredientLabel.setTextColor(colorGrey);
        llIngredients.setVisibility(View.GONE);

    }

    private void ShowEditIngredients(int displayWidth) {
        ingredientsShown = true;
        tvIngredientLabel.setTextColor(Color.rgb(0,0,0));
        imgvIngredientSymbol.setImageResource(R.drawable.ic_expand_less_black_24dp);
        if (selectedRecipe.getIngredients() != null) {
            if (selectedRecipe.getIngredients().size() > 0) {
                llIngredients.setVisibility(View.VISIBLE);
                for (Ingredient currentIngredient : selectedRecipe.getIngredients()) {
                    //Layout per Ingredient
                    LinearLayout horizontalEditIngredientLayout = new LinearLayout(EditRecipeActivity.this);
                    LinearLayout.LayoutParams horizontalEditIngredientLayoutParams =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    horizontalEditIngredientLayout.setLayoutParams(horizontalEditIngredientLayoutParams);
                    horizontalEditIngredientLayout.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalEditIngredientLayout.setPadding(100,5,100,5);

                    //IngredientName
                    EditText etIngredientName = new EditText(EditRecipeActivity.this);
                    LinearLayout.LayoutParams etIngredientNameParams =
                            new LinearLayout.LayoutParams(displayWidth/2-125, ViewGroup.LayoutParams.WRAP_CONTENT);
                    etIngredientName.setLayoutParams(etIngredientNameParams);
                    etIngredientName.setText(currentIngredient.getName());
                    etIngredientName.setTextColor(colorGrey);

                    //IngredientAmount
                    EditText etIngredientAmount = new EditText(EditRecipeActivity.this);
                    LinearLayout.LayoutParams etIngredientAmountParams =
                            new LinearLayout.LayoutParams(displayWidth/4-125, ViewGroup.LayoutParams.WRAP_CONTENT );
                    etIngredientAmount.setLayoutParams(etIngredientAmountParams);
                    etIngredientAmount.setText(String.valueOf(currentIngredient.getAmount()));
                    etIngredientAmount.setTextColor(colorGrey);

                    //IngredientUnit
                    ArrayList<String> spinnerArray = new ArrayList<String>();
                    spinnerArray.add(Unit.Unit.toString());
                    spinnerArray.add(Unit.Gram.toString());
                    spinnerArray.add(Unit.Liter.toString());
                    spinnerArray.add(Unit.CoffeeSpoon.toString());

                    Spinner newIngredientSpinner = new Spinner(EditRecipeActivity.this);
                    ArrayAdapter<String> spinnerArrayAdapter =
                            new ArrayAdapter<String>(EditRecipeActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    newIngredientSpinner.setAdapter(spinnerArrayAdapter);
                    LinearLayout.LayoutParams newIngredientSpinnerParams
                            = new LinearLayout.LayoutParams(displayWidth/4+250, ViewGroup.LayoutParams.WRAP_CONTENT);
                    newIngredientSpinner.setLayoutParams(newIngredientSpinnerParams);
                    newIngredientSpinner.setPrompt("Unit");
                    newIngredientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (view != null && adapterView != null){
                                ((TextView)adapterView.getChildAt(0)).setTextColor(colorGrey);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    for (int i = 0; i < spinnerArray.size(); i++) {
                        if (currentIngredient.getAmountUnit().toString().contains(spinnerArray.get(i))) {
                            newIngredientSpinner.setSelection(i);
                        }
                    }

                    //Add views to superview
                    horizontalEditIngredientLayout.addView(etIngredientName);
                    horizontalEditIngredientLayout.addView(etIngredientAmount);
                    horizontalEditIngredientLayout.addView(newIngredientSpinner);
                    llIngredients.addView(horizontalEditIngredientLayout);
                }
            }
        }



    }

    private void ShowEditDescription(int displayWidth) {
        descriptionShown = true;
        tvDescriptionLabel.setTextColor(Color.rgb(0,0,0));
        imgvDescriptionSymbol.setImageResource(R.drawable.ic_expand_less_black_24dp);
        llDescription.setVisibility(View.VISIBLE);

        EditText etDescription = new EditText(EditRecipeActivity.this);
        LinearLayout.LayoutParams etDescriptionParams = new LinearLayout.LayoutParams(((displayWidth/2)+(displayWidth/4)), ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        etDescription.setLayoutParams(etDescriptionParams);
        //etDescription.setPadding(100,0,100,0);
        etDescription.setSingleLine(false);
        //etDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        etDescription.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        //etDescription.setLines(5);
        //etDescription.setMaxLines(10);
        etDescription.setHint("Type here...");
        etDescription.setTextColor(colorGrey);
        //etDescription.setBackgroundColor(Color.parseColor("#efefef"));

        if (selectedRecipe != null) {
            if (selectedRecipe.getDescription() != null) {
                if (!selectedRecipe.getDescription().equals("")) {
                    etDescription.setText(selectedRecipe.getDescription());
                }
            }
        }

        llDescription.addView(etDescription);

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
