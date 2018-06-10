package com.hagenberg.needy.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.hagenberg.needy.R;

public class ShowAllRecipeBooksFragment extends Fragment {

    TextView tvFilter;
    Spinner spFilter;
    RecyclerView rvRecipeBooks;
    FloatingActionButton fabAddRecipeBook;

    public ShowAllRecipeBooksFragment() {
        // Required empty public constructor
    }

    public static ShowAllRecipeBooksFragment newInstance() {
        ShowAllRecipeBooksFragment fragment = new ShowAllRecipeBooksFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_show_all_recipe_books, container, false);

        tvFilter = rootView.findViewById(R.id.show_recipe_books_tv_filter);
        spFilter = rootView.findViewById(R.id.show_recipe_books_sp_filter);
        rvRecipeBooks = rootView.findViewById(R.id.show_recipe_books_rv_recipe_books);
        fabAddRecipeBook = rootView.findViewById(R.id.show_recipe_books_fab_add_recipe_book);

        return rootView;
    }
}
