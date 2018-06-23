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
    RecipeViewModel recipeViewModel;
    String searchString;
    RecyclerView rvRecipes;
    FloatingActionButton fabAddRecipe;
    RecyclerView.LayoutManager layoutManager;
    ShowAllRecipesListAdapter listAdapter = new ShowAllRecipesListAdapter(new LinkedList<Recipe>());

    public void setSearchString(String searchString) {
        this.searchString = searchString;
        //Update recipes with this search string

        setUpListAdapter(searchString);
        //Toast.makeText(getContext(), "Updating this views layouts with new search string: " + searchString, Toast.LENGTH_LONG).show();
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

        setUpListAdapter("");

        return rootView;

    }

    private void setUpListAdapter(final String searchString) {
        recipeViewModel = ViewModelProviders.of(this.getActivity()).get(RecipeViewModel.class);
        insertTestValues(recipeViewModel);
        LiveData<List<Recipe>> allRecipes = recipeViewModel.getAllRecipes();
        List<Recipe> recipeList = allRecipes.getValue();

        if(recipeList==null) {
            recipeList = new LinkedList<Recipe>();
            listAdapter = new ShowAllRecipesListAdapter(recipeList);
            rvRecipes.setAdapter(listAdapter);
        } else {
            recipeList = searchRecipeList(recipeList, searchString);
            listAdapter = new ShowAllRecipesListAdapter(recipeList);
            rvRecipes.setAdapter(listAdapter);
        }
        allRecipes.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable final List<Recipe> recipes) {
                // allRecipes hat sich geändert (new entry, deleted entry, async insert, ...)
                if(recipes != null){
                    //reload RecyclerView with new RecipeValues
                    List<Recipe> searchedRecipes = searchRecipeList(recipes, searchString);
                    listAdapter.updateData(searchedRecipes);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private List<Recipe> searchRecipeList(List<Recipe> recipeList, String searchString) {
        if (searchString == "") {
            return recipeList;
        }

        List<Recipe> searchedRecipeList = new LinkedList<Recipe>();
        for(Recipe recipe : recipeList) {
            if(recipe.getName().startsWith(searchString)){
                searchedRecipeList.add(recipe);
            }
        }
        return searchedRecipeList;
    }


    private void insertTestValues(RecipeViewModel rvm) {
        List<Ingredient> ingredients = new ArrayList<>();
        Recipe rec = new Recipe("Recipe1", "Description of Recipe1", ingredients);
        Recipe rec1 = new Recipe("Recipe2", "sweg", ingredients);
        rvm.insert(rec);
        rvm.insert(rec1);
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
