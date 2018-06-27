package com.hagenberg.needy.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.IngredientViewId;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.Unit;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateRecipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRecipeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateReceiptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateRecipeFragment newInstance(String param1, String param2) {
        CreateRecipeFragment fragment = new CreateRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    EditText et_create_recipe_name;
    Button bt_new_ingredient, bt_continue;
    LinearLayout ll_ingredients;
    private ImageView imgvAddSymbol;

    //hier werden alle Ids zu den dynamisch erzeugten Ingredient Views gespeichert um
    //ihre Inhalte später abrufen zu können
    List<IngredientViewId> ingredientViewIdList = new ArrayList<>();
    int idCounter = 13400;

    boolean infoValid = true;
    List<Ingredient> ingredients = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_create_recipe, container, false);
        et_create_recipe_name = rootView.findViewById(R.id.et_create_recipe_name);
        //bt_new_ingredient = rootView.findViewById(R.id.bt_create_recipe_new_ingredient);
        bt_continue = rootView.findViewById(R.id.bt_create_recipe_saverecipe);
        ll_ingredients = rootView.findViewById(R.id.ll_create_recipe_ingredients);
        imgvAddSymbol = rootView.findViewById(R.id.img_create_recipe_ingredients_add);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int displayWidth = size.x;

        //Dass schon einmal ein Ingredient angezeigt wird, wenn das Fragment geladen wurde
        CreateNewIngredientView(displayWidth);


        imgvAddSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewIngredientView(displayWidth);
            }
        });

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoValid = true;
                final String recipeName = et_create_recipe_name.getText().toString();
                if (!recipeName.equals("")) {
                    if (!ingredientViewIdList.isEmpty()) {
                        for (IngredientViewId ingredientIds : ingredientViewIdList) {
                            EditText etName = rootView.findViewById(ingredientIds.getNameId());
                            String name = etName.getText().toString();

                            EditText etAmount = rootView.findViewById(ingredientIds.getAmountId());
                            String amount = etAmount.getText().toString();

                            if (name.equals("")) {
                                //Toast.makeText(getActivity(), "Infos fehlen", Toast.LENGTH_SHORT).show();
                                //etName.startAnimation(shakeError());
                                infoValid = false;
                            }
                            if (amount.equals("")) {
                                //Toast.makeText(getActivity(), "Infos fehlen", Toast.LENGTH_SHORT).show();
                                //etAmount.startAnimation(shakeError());
                                infoValid = false;
                            }

                            if (infoValid == true) {
                                Spinner spUnit = rootView.findViewById(ingredientIds.getUnitId());
                                Unit unit = Unit.valueOf(spUnit.getSelectedItem().toString());
                                int numberAmount = Integer.parseInt(amount);
                                Ingredient ingredient = new Ingredient(name, numberAmount, unit);
                                ingredients.add(ingredient);

                                } else {
                                ingredients.clear();

                            }

                        }

                        if (infoValid == true){
                            openDialogToFinish(recipeName);
                        } else {
                            Toast.makeText(getActivity(), "All fields must be filled...", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Recipe needs a name...", Toast.LENGTH_SHORT).show();
                }

            }
        });




        return rootView;
    }

    private void openDialogToFinish(final String recipeName) {
        final Dialog dialogDescription = new Dialog(getActivity(), R.style.DescriptionDialog);
        dialogDescription.setContentView(R.layout.dialog_create_recipe_description);
        Button btSave = dialogDescription.findViewById(R.id.bt_create_recipe_dialog_save);
        Button btCancel = dialogDescription.findViewById(R.id.bt_create_recipe_dialog_cancel);
        final EditText etDescription = dialogDescription.findViewById(R.id.et_create_recipe_dialog_description);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDescription.dismiss();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etDescription.getText().toString();
                final Recipe recipe = new Recipe(recipeName, description, ingredients);
                //In Datenbank speichern
                RecipeViewModel recipeViewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
                recipeViewModel.insert(recipe);


                LiveData<List<Recipe>> allRecipes = recipeViewModel.getAllRecipes();
                List<Recipe> recipesOutsideOfLiveData = allRecipes.getValue();
                allRecipes.observe(getActivity(), new Observer<List<Recipe>>() {
                    @Override
                    public void onChanged(@Nullable final List<Recipe> recipes) {
                        // allRecipes hat sich geändert (new entry, deleted entry, async insert, ...)
                        if(recipes != null){
                            if (recipes.size() > 0) {
                                if (recipes.get(recipes.size()-1).getName().equals(recipe.getName())) {
                                    Toast.makeText(getActivity(), "Recipe created", Toast.LENGTH_SHORT).show();
                                    dialogDescription.dismiss();
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), "Error at creating the Recipe", Toast.LENGTH_SHORT).show();
                                    dialogDescription.dismiss();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            dialogDescription.dismiss();
                        }
                    }
                });
            }
        });
        dialogDescription.show();
    }

    private TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    private void CreateNewIngredientView(int displayWidth) {
        //create vertical layout to fit 2 horizontal layout
        final LinearLayout llnewIngredientVertical = new LinearLayout(getActivity());
        llnewIngredientVertical.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llnewIngredientVertical.setOrientation(LinearLayout.VERTICAL);
        llnewIngredientVertical.setPadding(0,15,0,15);

        //first horizontal layout fitting name and amount
        LinearLayout llnewIngredientHorizontal1 = new LinearLayout(getActivity());
        llnewIngredientHorizontal1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llnewIngredientHorizontal1.setOrientation(LinearLayout.HORIZONTAL);
        llnewIngredientHorizontal1.setPadding(0,0,0,0);

        //second horizontal layout fitting unit and delete
        LinearLayout llnewIngredientHorizontal2 = new LinearLayout(getActivity());
        llnewIngredientHorizontal2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llnewIngredientHorizontal2.setOrientation(LinearLayout.HORIZONTAL);
        llnewIngredientHorizontal2.setPadding(0,0,0,0);

        EditText etnewIngredientName = new EditText(getActivity());
        etnewIngredientName.setLayoutParams(new LinearLayout.LayoutParams(displayWidth/2, ViewGroup.LayoutParams.MATCH_PARENT));
        etnewIngredientName.setHint("Name");
        etnewIngredientName.setId(idCounter++);

        EditText etNewIngredientAmount = new EditText(getActivity());
        etNewIngredientAmount.setLayoutParams(new LinearLayout.LayoutParams(displayWidth/2, ViewGroup.LayoutParams.MATCH_PARENT));
        etNewIngredientAmount.setHint("Amount");
        etNewIngredientAmount.setInputType(InputType.TYPE_CLASS_NUMBER |  InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etNewIngredientAmount.setId(idCounter++);

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(Unit.Unit.toString());
        spinnerArray.add(Unit.CoffeeSpoon.toString());
        spinnerArray.add(Unit.Gram.toString());
        spinnerArray.add(Unit.Liter.toString());


        Spinner newIngredientSpinner = new Spinner(getActivity());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        newIngredientSpinner.setAdapter(spinnerArrayAdapter);
        newIngredientSpinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        newIngredientSpinner.setPrompt("Unit");
        newIngredientSpinner.setId(idCounter++);

        //View fillerView = new View(getActivity());
        //fillerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        ImageView imgDeleteIngredient = new ImageView(getActivity());
        imgDeleteIngredient.setImageResource(R.drawable.ic_delete_grey_24dp);
        LinearLayout.LayoutParams imgDeleteIngredientParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Resources r = getResources();
        //float dp20 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
        //imgDeleteIngredient.setPadding(0,(int)dp20,0,0);
        float dp25 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
        imgDeleteIngredientParams.setMargins(0,0,(int)dp25, 0);
        imgDeleteIngredient.setLayoutParams(imgDeleteIngredientParams);

        llnewIngredientHorizontal1.addView(etnewIngredientName);
        llnewIngredientHorizontal1.addView(etNewIngredientAmount);
        llnewIngredientHorizontal2.addView(newIngredientSpinner);
        llnewIngredientHorizontal2.addView(imgDeleteIngredient);

        llnewIngredientVertical.addView(llnewIngredientHorizontal1);
        llnewIngredientVertical.addView(llnewIngredientHorizontal2);

        final IngredientViewId ingredientViewId = new IngredientViewId(etnewIngredientName.getId(), etNewIngredientAmount.getId(), newIngredientSpinner.getId());
        ingredientViewIdList.add(ingredientViewId);

        View seperatorLine = new View(getActivity());
        seperatorLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        seperatorLine.setBackgroundColor(Color.rgb(181,181,181));

        llnewIngredientVertical.setPadding(0,100,0,100);
        ll_ingredients.addView(llnewIngredientVertical);
        ll_ingredients.addView(seperatorLine);

        imgDeleteIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_ingredients.removeView(llnewIngredientVertical);
                ingredientViewIdList.remove(ingredientViewId);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
