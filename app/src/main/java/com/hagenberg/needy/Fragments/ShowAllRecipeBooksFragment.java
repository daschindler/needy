package com.hagenberg.needy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Activities.CreateRecipeBookActivity;
import com.hagenberg.needy.R;

public class ShowAllRecipeBooksFragment extends Fragment {

    String searchString;
    TextView tvFilter;
    Spinner spFilter;
    RecyclerView rvRecipeBooks;
    FloatingActionButton fabAddRecipeBook;

    public ShowAllRecipeBooksFragment() {
        // Required empty public constructor
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
        Toast.makeText(getContext(), "Updating this views layouts with new search string: " + searchString, Toast.LENGTH_LONG).show();
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
        fabAddRecipeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateRecipeBookActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }
}
