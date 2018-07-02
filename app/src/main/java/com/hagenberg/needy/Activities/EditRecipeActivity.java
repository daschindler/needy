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
import android.text.InputType;
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
import java.util.List;

/**
 * ActivityClass for editing a Recipe Book.
 */
public class EditRecipeActivity extends AppCompatActivity {

    private int recipeId = 404040;

    private Recipe selectedRecipe;

    private LinearLayout llDescriptionButton, llIngredientButton, llIngredients, llDescription;

    private Button btSaveEditedRecipe;

    private EditText etRecipeName;

    //will be generated in ShowEditDescription
    private EditText etDescription;

    private TextView tvIngredientLabel, tvDescriptionLabel;
    private ImageView imgvIngredientSymbol, imgvDescriptionSymbol, imgvInfoSymbol;

    private ArrayList<View> viewIngredientList = new ArrayList<>();

    private ColorStateList colorGrey;

    private int firstStart = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        final RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
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
        imgvInfoSymbol = findViewById(R.id.img_edit_recipe_info);

        final int displayWidth = getDisplayWidth();

        llDescription.setPadding(displayWidth/8,0,displayWidth/8,55);
        llIngredients.setPadding(0,0,0,35);
        llDescription.setVisibility(View.GONE);
        llIngredients.setVisibility(View.GONE);

        colorGrey = tvIngredientLabel.getTextColors();

        HideSoftKeyboard();

        if (recipeId != 404040) {
            final LiveData<Recipe> selectedLiveRecipe = recipeViewModel.getRecipeById(recipeId);
            selectedLiveRecipe.observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable final Recipe recipe) {
                    if(recipe != null){
                        if (firstStart == 0) {
                            selectedRecipe = recipe;
                            getSupportActionBar().setTitle("Edit " + selectedRecipe.getName());
                            etRecipeName.setText(selectedRecipe.getName());
                            ShowEditIngredients(displayWidth);
                            ShowEditDescription(displayWidth);
                            firstStart++;
                        }
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

            imgvInfoSymbol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(EditRecipeActivity.this, "If no name is filled, ingredient will be deleted...", Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if( v != null) v.setGravity(Gravity.CENTER);
                    toast.show();
                }
            });

            btSaveEditedRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SaveEditedRecipe(recipeViewModel);
                }
            });

        } else {
            Toast.makeText(this, "Error loading Recipe :(", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * The curent state of the Recipeview will be updated to the Database and
     * the view will be closed.
     * @param recipeViewModel
     * Is needed for operating with the database
     */
    private void SaveEditedRecipe(RecipeViewModel recipeViewModel) {
        String newName = etRecipeName.getText().toString();
        String newDescription = "";
        if (etDescription.getText() != null) {
            newDescription = etDescription.getText().toString();
        }

        List<Ingredient> newIngredients = new ArrayList<>();
        for (int i = 0; i < viewIngredientList.size(); i++) {
                EditText editTextName = (EditText)viewIngredientList.get(i+0);
                EditText editTextAmount = (EditText)viewIngredientList.get(i+1);
                Spinner spUnit = (Spinner)viewIngredientList.get(i+2);

                i = i +2;

                if (!editTextName.getText().toString().equals("")){
                    String newIngName = editTextName.getText().toString();
                    Double newIngAmountDouble = 0.0;
                    if (!editTextAmount.getText().toString().equals("")) {
                        newIngAmountDouble = Double.valueOf(editTextAmount.getText().toString());
                    }
                    Unit newIngUnit = Unit.valueOf(spUnit.getSelectedItem().toString());

                    Ingredient newIngredient = new Ingredient(newIngName, newIngAmountDouble, newIngUnit);
                    newIngredients.add(newIngredient);
                }
        }

        selectedRecipe.setName(newName);
        selectedRecipe.setDescription(newDescription);
        selectedRecipe.setIngredients(newIngredients);

        recipeViewModel.update(selectedRecipe);


        EditRecipeActivity.this.finish();
    }

    /**
     * The softkeyboard will be hidden, if this function is called
     */
    private void HideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
     * Shows all ingredients of the current recipe
     * @param displayWidth
     */
    private void ShowEditIngredients(final int displayWidth) {
        tvIngredientLabel.setTextColor(Color.rgb(0,0,0));
        if (selectedRecipe.getIngredients() != null) {
            if (selectedRecipe.getIngredients().size() > 0) {
                llIngredients.setVisibility(View.VISIBLE);
                for (Ingredient currentIngredient : selectedRecipe.getIngredients()) {
                    AddIngredientToView(currentIngredient, displayWidth);
                }
            }
        }

        imgvIngredientSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddIngredientToView(new Ingredient("",0.0,Unit.Unit), displayWidth);
            }
        });
    }

    /**
     * Adds one specified ingredient to the View
     * @param currentIngredient
     * The ingredient that should be added to the View
     * @param displayWidth
     * Is needed for a good looking layout on every device
     */
    private void AddIngredientToView(Ingredient currentIngredient, int displayWidth) {
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
        viewIngredientList.add(etIngredientName);

        //IngredientAmount
        EditText etIngredientAmount = new EditText(EditRecipeActivity.this);
        LinearLayout.LayoutParams etIngredientAmountParams =
                new LinearLayout.LayoutParams(displayWidth/4-125, ViewGroup.LayoutParams.WRAP_CONTENT );
        etIngredientAmount.setLayoutParams(etIngredientAmountParams);
        if (currentIngredient.getAmount() == 0) {
            etIngredientAmount.setText("");
        } else {
            etIngredientAmount.setText(String.valueOf(currentIngredient.getAmount()));
        }
        etIngredientAmount.setInputType(InputType.TYPE_CLASS_NUMBER |  InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etIngredientAmount.setTextColor(colorGrey);
        viewIngredientList.add(etIngredientAmount);

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
        viewIngredientList.add(newIngredientSpinner);

        //Add views to superview
        horizontalEditIngredientLayout.addView(etIngredientName);
        horizontalEditIngredientLayout.addView(etIngredientAmount);
        horizontalEditIngredientLayout.addView(newIngredientSpinner);
        llIngredients.addView(horizontalEditIngredientLayout);
    }

    /**
     * Shows the description of the current Recipe
     * @param displayWidth
     * Is needed for a good looking layout on every device
     */
    private void ShowEditDescription(int displayWidth) {
        tvDescriptionLabel.setTextColor(Color.rgb(0,0,0));
        llDescription.setVisibility(View.VISIBLE);

        etDescription = new EditText(EditRecipeActivity.this);
        LinearLayout.LayoutParams etDescriptionParams = new LinearLayout.LayoutParams(((displayWidth/2)+(displayWidth/4)), ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        etDescription.setLayoutParams(etDescriptionParams);
        etDescription.setSingleLine(false);
        etDescription.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        etDescription.setHint("Type here...");
        etDescription.setTextColor(colorGrey);

        if (selectedRecipe != null) {
            if (selectedRecipe.getDescription() != null) {
                if (!selectedRecipe.getDescription().equals("")) {
                    etDescription.setText(selectedRecipe.getDescription());
                }
            }
        }

        llDescription.addView(etDescription);

        imgvDescriptionSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDescription.setText("");
            }
        });

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
