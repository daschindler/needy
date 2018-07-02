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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Activities.CreateRecipeBookActivity;
import com.hagenberg.needy.Adapters.ShowAllRecipeBooksListAdapter;
import com.hagenberg.needy.Entity.RecipeBook;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeBookViewModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Shows all RecipeBooks to display in this fragment
 */
public class ShowAllRecipeBooksFragment extends Fragment {
    RecipeBookViewModel recipeBookViewModel;
    String searchString = "";
    RecyclerView rvRecipeBooks;
    FloatingActionButton fabAddRecipeBook;
    RecyclerView.LayoutManager layoutManager;
    ShowAllRecipeBooksListAdapter listAdapter = new ShowAllRecipeBooksListAdapter(new LinkedList<RecipeBook>());

    public ShowAllRecipeBooksFragment() {
        // Required empty public constructor
    }

    /**
     * Setter for searchString, after a new searchString is set, the recyclerview is reloaded with the filtered recipebooks.
     * @param searchString
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
        initializeRecyclerView(searchString);
    }

    /**
     * Creates a new instance of this fragment, used for the ViewPagerAdapater
     * @return
     */
    public static ShowAllRecipeBooksFragment newInstance() {
        ShowAllRecipeBooksFragment fragment = new ShowAllRecipeBooksFragment();
        return fragment;
    }

    /**
     * Called when Fragment is created newly.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called when Fragment views are created newly. In this method the views get inflated, and then the InitializeRecyclerView-Method is called.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_show_all_recipe_books, container, false);

        rvRecipeBooks = rootView.findViewById(R.id.show_recipe_books_rv_recipe_books);
        layoutManager = new LinearLayoutManager(getActivity());
        rvRecipeBooks.setLayoutManager(layoutManager);

        fabAddRecipeBook = rootView.findViewById(R.id.show_recipe_books_fab_add_recipe_book);
        fabAddRecipeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateRecipeBookActivity.class);
                getActivity().startActivity(intent);
            }
        });

        initializeRecyclerView(searchString);

        return rootView;
    }

    /**
     * Inits the RecyclerView and its adapter with the recipeBooks from the database. Also only uses the filtered RecipeBooks, if a searchString other than
     * "" is set. If RecipeBooks are changed in the db, this method reacts as well since it sets an Observer to changes of RecipeBooks.
     * @param searchString
     */
    private void initializeRecyclerView(final String searchString) {
        recipeBookViewModel = ViewModelProviders.of(this.getActivity()).get(RecipeBookViewModel.class);
        LiveData<List<RecipeBook>> liveRecipeBooks = recipeBookViewModel.getAllLiveRecipeBooks();
        List<RecipeBook> recipeBooks = liveRecipeBooks.getValue();
        if(recipeBooks == null) {
            recipeBooks = new LinkedList<RecipeBook>();
        }
        recipeBooks = searchRecipeBooks(recipeBooks, searchString);
        listAdapter = new ShowAllRecipeBooksListAdapter(recipeBooks);
        rvRecipeBooks.setAdapter(listAdapter);
        liveRecipeBooks.observe(this, new Observer<List<RecipeBook>>() {
            @Override
            public void onChanged(@Nullable List<RecipeBook> recipeBooks) {
                if(recipeBooks != null) {
                    recipeBooks = searchRecipeBooks(recipeBooks, searchString);
                    listAdapter.updateData(recipeBooks);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    /**
     * Filters the recipeBooks parameter with the searchString, and returns the filtered recipe books.
     * @param recipeBooks
     * @param searchString
     * @return
     */
    private LinkedList<RecipeBook> searchRecipeBooks(List<RecipeBook> recipeBooks, String searchString) {
        LinkedList<RecipeBook> searchedBooks = new LinkedList<RecipeBook>();

        if(recipeBooks != null){
            for(RecipeBook book : recipeBooks) {
                if(book.getName().toLowerCase().startsWith(searchString.toLowerCase())){
                    searchedBooks.add(book);
                }
            }
        }
        return searchedBooks;
    }
}
