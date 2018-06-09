package com.hagenberg.needy.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.hagenberg.needy.R;

public class ShowAllRecipesFragment extends Fragment {
    Spinner spinnerFilter;
    RecyclerView rvRecipes;
    FloatingActionButton fabAddRecipe;

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
        spinnerFilter = (Spinner) rootView.findViewById(R.id.show_recipes_sp_filter);
        rvRecipes = (RecyclerView) rootView.findViewById(R.id.show_recipes_rv_recipes);
        fabAddRecipe = (FloatingActionButton) rootView.findViewById(R.id.show_recipes_fab_add_recipe);

        //do setup stuff

        return rootView;

    }

}
