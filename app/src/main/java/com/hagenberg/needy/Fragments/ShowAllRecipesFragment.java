package com.hagenberg.needy.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.hagenberg.needy.Adapters.ShowAllRecipesListAdapter;
import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShowAllRecipesFragment extends Fragment {
    Spinner spinnerFilter;
    RecyclerView rvRecipes;
    FloatingActionButton fabAddRecipe;
    RecyclerView.LayoutManager layoutManager;

    public ShowAllRecipesFragment() {
        // Required empty public constructor
    }

    public static ShowAllRecipesFragment newInstance() {
        ShowAllRecipesFragment fragment = new ShowAllRecipesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_show_all_recipes, container, false);
        spinnerFilter = rootView.findViewById(R.id.show_recipes_sp_filter);
        rvRecipes = rootView.findViewById(R.id.show_recipes_rv_recipes);
        fabAddRecipe = rootView.findViewById(R.id.show_recipes_fab_add_recipe);
        layoutManager = new LinearLayoutManager(getActivity());
        rvRecipes.setLayoutManager(layoutManager);

        RecipeViewModel recipeViewModel = ViewModelProviders.of(this.getActivity()).get(RecipeViewModel.class);
        LiveData<List<Recipe>> allRecipes = recipeViewModel.getAllRecipes();
        List<Recipe> recipeList = allRecipes.getValue();

        //Test values for adapter
        List<Ingredient> ingredients = new ArrayList<>();
        Recipe rec = new Recipe("Recipe1", "Description of Recipe1", ingredients);
        recipeList = new LinkedList<Recipe>();
        recipeList.add(rec);


        ShowAllRecipesListAdapter listAdapter = new ShowAllRecipesListAdapter(recipeList);
        rvRecipes.setAdapter(listAdapter);


        allRecipes.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable final List<Recipe> recipes) {
                // allRecipes hat sich geändert (new entry, deleted entry, async insert, ...)
                if(recipes != null){
                    //reload RecyclerView with new RecipeValues
                }
            }
        });

        //do setup stuff

        return rootView;

    }

}
