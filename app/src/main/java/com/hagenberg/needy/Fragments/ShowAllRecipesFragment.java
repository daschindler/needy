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
import android.widget.Spinner;
import android.widget.Toast;

import com.hagenberg.needy.Activities.CreateRecipeActivity;
import com.hagenberg.needy.Adapters.ShowAllRecipesListAdapter;
import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShowAllRecipesFragment extends Fragment {
    String searchString;
    Spinner spinnerFilter;
    RecyclerView rvRecipes;
    FloatingActionButton fabAddRecipe;
    RecyclerView.LayoutManager layoutManager;
    ShowAllRecipesListAdapter listAdapter = new ShowAllRecipesListAdapter(new LinkedList<Recipe>());

    public void setSearchString(String searchString) {
        this.searchString = searchString;
        //Update recipes with this search string
        Toast.makeText(getContext(), "Updating this views layouts with new search string: " + searchString, Toast.LENGTH_LONG).show();
    }

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
        layoutManager = new LinearLayoutManager(getActivity());
        rvRecipes.setLayoutManager(layoutManager);

        fabAddRecipe = rootView.findViewById(R.id.show_recipes_fab_add_recipe);
        fabAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateRecipeActivity.class);
                getActivity().startActivity(intent);
            }
        });

        RecipeViewModel recipeViewModel = ViewModelProviders.of(this.getActivity()).get(RecipeViewModel.class);
        LiveData<List<Recipe>> allRecipes = recipeViewModel.getAllRecipes();
        List<Recipe> recipeList = allRecipes.getValue();

        //Only set up RecyclerView, if the dataset contains values
        if(recipeList!=null) {
            //Test values for adapter
//            List<Ingredient> ingredients = new ArrayList<>();
//            Recipe rec = new Recipe("Recipe1", "Description of Recipe1", ingredients);
//            recipeList = new LinkedList<Recipe>();
//            recipeList.add(rec);

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

        return rootView;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.main_menu_search:

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
