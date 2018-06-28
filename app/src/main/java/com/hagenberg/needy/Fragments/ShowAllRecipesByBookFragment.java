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
import android.widget.TextView;

import com.hagenberg.needy.Activities.CreateRecipeActivity;
import com.hagenberg.needy.Adapters.ShowAllRecipesListAdapter;
import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeBookViewModel;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShowAllRecipesByBookFragment extends Fragment {
    private RecipeViewModel recipeViewModel;
    private String searchString;
    private RecyclerView rvRecipes;
    private FloatingActionButton fabAddRecipe;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvBookDesc;
    private ShowAllRecipesListAdapter listAdapter = new ShowAllRecipesListAdapter(new LinkedList<Recipe>(), getContext());

    RecipeBook selectedRecipeBook;

    public ShowAllRecipesByBookFragment() { }

    public void setRecipeBook(RecipeBook book){
        this.selectedRecipeBook = book;
    }

    public static ShowAllRecipesByBookFragment newInstance(RecipeBook book) {
        ShowAllRecipesByBookFragment fragment = new ShowAllRecipesByBookFragment();
        fragment.setRecipeBook(book);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_all_recipes_by_book, container, false);

        rvRecipes = rootView.findViewById(R.id.show_recipes_rv_recipes);

        layoutManager = new LinearLayoutManager(getActivity());
        rvRecipes.setLayoutManager(layoutManager);
        tvBookDesc = rootView.findViewById(R.id.tv_bookDesc);

        RecipeBookViewModel recipeBookViewModel = ViewModelProviders.of(this).get(RecipeBookViewModel.class);
        final LiveData<RecipeBook> recipeBook = recipeBookViewModel.getRecipeBookById(selectedRecipeBook.getUid());

        recipeBook.observe(this, new Observer<RecipeBook>() {
            @Override
            public void onChanged(@Nullable final RecipeBook recipebook) {
                if(recipebook != null){
                    selectedRecipeBook = recipebook;
                    tvBookDesc.setText(recipebook.getDescription());

                    if(listAdapter != null){
                        listAdapter.updateData(selectedRecipeBook.getRecipies());
                        listAdapter.notifyDataSetChanged();
                    }
                }else{
                    //This Book got deleted!
                }
            }
        });

        setUpListAdapter();

        return rootView;
    }

    private void setUpListAdapter() {
        List<Recipe> recipeList = selectedRecipeBook.getRecipies();

        if(recipeList==null) {
            recipeList = new LinkedList<>();
            listAdapter = new ShowAllRecipesListAdapter(recipeList, getContext());
            rvRecipes.setAdapter(listAdapter);
        } else {
            listAdapter = new ShowAllRecipesListAdapter(recipeList, getContext());
            rvRecipes.setAdapter(listAdapter);
        }
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
