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


/**
 * @author thomasmaier
 * Fragment with the Views for the List of all Recipes. Used in the MainPagerAdapter
 */
public class ShowAllRecipesFragment extends Fragment {
    RecipeViewModel recipeViewModel;
    String searchString;
    RecyclerView rvRecipes;
    FloatingActionButton fabAddRecipe;
    RecyclerView.LayoutManager layoutManager;
    ShowAllRecipesListAdapter listAdapter = new ShowAllRecipesListAdapter(new LinkedList<Recipe>(), getContext());

    /**
     * Setter for the searchString property, sets the new searchString for this fragment.
     * @param searchString
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
        setUpListAdapter(searchString);
    }

    public ShowAllRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Used in the MainPagerAdapter for creating a new Instance of the Fragment.
     * @return An instance of the created fragment.
     */
    public static ShowAllRecipesFragment newInstance() {
        ShowAllRecipesFragment fragment = new ShowAllRecipesFragment();
        return fragment;
    }

    /**
     * Called when the fragment is created.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called when the views are created, inflates the layouts and calls the Setup-Method for the RecyclerView.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Returns the inflated Fragment as a view.
     */
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

    /**
     * Sets up the list adapter with the recipe-values from the database, and puts an observer on them. After changes to the recipes, the observer is called and
     * changes the dataset of the list adapter to the new one. Also uses the filtered recipes, if searchString is something else than "".
     * @param searchString
     */
    private void setUpListAdapter(final String searchString) {
        recipeViewModel = ViewModelProviders.of(this.getActivity()).get(RecipeViewModel.class);
        LiveData<List<Recipe>> allRecipes = recipeViewModel.getAllRecipes();
        //insertTestValues(recipeViewModel);
        List<Recipe> recipeList = allRecipes.getValue();

        if(recipeList==null) {
            recipeList = new LinkedList<Recipe>();
            listAdapter = new ShowAllRecipesListAdapter(recipeList, getContext());
            rvRecipes.setAdapter(listAdapter);
        } else {
            recipeList = searchRecipeList(recipeList, searchString);
            listAdapter = new ShowAllRecipesListAdapter(recipeList, getContext());
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
                else {
                    listAdapter.updateData(new LinkedList<Recipe>());
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Filters the recipeList by the searchString parameter given, and returns the filtered Recipes.
     * @param recipeList
     * @param searchString
     * @return A filtered list containing only items with names starting with the searchstring-parameter.
     */
    private List<Recipe> searchRecipeList(List<Recipe> recipeList, String searchString) {
        if (searchString == "") {
            return recipeList;
        }

        List<Recipe> searchedRecipeList = new LinkedList<Recipe>();
        for(Recipe recipe : recipeList) {
            if(recipe.getName().toLowerCase().startsWith(searchString.toLowerCase())){
                searchedRecipeList.add(recipe);
            }
        }
        return searchedRecipeList;
    }


    /**
     * Inserts test values into the database, not used anymore.
     * @param rvm
     */
    private void insertTestValues(RecipeViewModel rvm) {
        List<Ingredient> ingredients = new ArrayList<>();
        Recipe rec = new Recipe("Recipe1", "Description of Recipe1", ingredients);
        Recipe rec1 = new Recipe("Recipe2", "sweg", ingredients);
        rvm.insert(rec);
        rvm.insert(rec1);
    }
}
