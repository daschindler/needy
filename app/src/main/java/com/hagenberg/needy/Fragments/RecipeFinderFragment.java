package com.hagenberg.needy.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hagenberg.needy.Activities.CreateRecipeActivity;
import com.hagenberg.needy.Adapters.ShowAllRecipesListAdapter;
import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecipeFinderFragment extends Fragment {
    //forthepush
    RecipeViewModel recipeViewModel;

    RecyclerView rvRecipes;
    RecyclerView rvIngredients;

    RecyclerView.LayoutManager layoutManager;
    ShowAllRecipesListAdapter listAdapter;

    public RecipeFinderFragment() {}

    public static RecipeFinderFragment newInstance() {
        RecipeFinderFragment fragment = new RecipeFinderFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_recipe_finder, container, false);

        //RecyclerView Recipe-List
        rvRecipes = rootView.findViewById(R.id.list_recipes);
        layoutManager = new LinearLayoutManager(getActivity());
        rvRecipes.setLayoutManager(layoutManager);

        //RecyclerView Ingredient-List
        rvIngredients = rootView.findViewById(R.id.rvIngredients);
        layoutManager = new LinearLayoutManager(getActivity());
        rvRecipes.setLayoutManager(layoutManager);

        setUpListAdapter();

        return rootView;

    }

    private void setUpListAdapter() {


        recipeViewModel = ViewModelProviders.of(this.getActivity()).get(RecipeViewModel.class);
        LiveData<List<Recipe>> allRecipes = recipeViewModel.getAllRecipes();
        List<Recipe> recipeList = allRecipes.getValue();

        if(recipeList==null) {
            recipeList = new LinkedList<Recipe>();
            listAdapter = new ShowAllRecipesListAdapter(recipeList);
            rvRecipes.setAdapter(listAdapter);
        } else {
            listAdapter = new ShowAllRecipesListAdapter(recipeList);
            rvRecipes.setAdapter(listAdapter);
        }

        allRecipes.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable final List<Recipe> recipes) {
                // allRecipes hat sich geändert (new entry, deleted entry, async insert, ...)
                if(recipes != null){
                    //reload RecyclerView with new RecipeValues
                    listAdapter.updateData(recipes);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.main_menu_search:
                //ToDo
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
