package com.hagenberg.needy.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hagenberg.needy.R;

public class ShowAllRecipeBooksFragment extends Fragment {
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
        return inflater.inflate(R.layout.fragment_show_all_recipe_books, container, false);
    }
}
