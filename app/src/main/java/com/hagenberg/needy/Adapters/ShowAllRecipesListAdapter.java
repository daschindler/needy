package com.hagenberg.needy.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.R;

import java.util.List;

public class ShowAllRecipesListAdapter extends RecyclerView.Adapter<ShowAllRecipesListAdapter.ViewHolder> {
    private List<Recipe> recipes;

    public boolean updateData(List<Recipe> recipes){
        if(recipes.size()>0) {
            this.recipes = recipes;
            return true;
        }
        return false;
    }

    public ShowAllRecipesListAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final String recipeName = recipes.get(i).getName();
        viewHolder.tvRecipeName.setText(recipeName);
        viewHolder.tvRecipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call detail view of clicked Recipe.
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRecipeName;
        public View rowLayout;

        public ViewHolder(View v) {
            super(v);
            rowLayout = v;
            tvRecipeName = v.findViewById(android.R.id.text1);
        }
    }
}
