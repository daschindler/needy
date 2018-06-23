package com.hagenberg.needy.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by thomasmaier on 08.05.18.
 */

public class CreateRecipeBookListAdapter extends RecyclerView.Adapter<CreateRecipeBookListAdapter.ViewHolder> {
    private List<Recipe> recipes;

    public void updateData(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public CreateRecipeBookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.create_recipe_book_row, parent, false);
        ViewHolder vh = new ViewHolder(view);   //Creates the list item views for the recipes
        return vh;
    }

    //Reduzieren auf ausschließlich Checkbox, Tv durch Text von Checkbox ersetzen (!!!)
    @Override
    public void onBindViewHolder(CreateRecipeBookListAdapter.ViewHolder vh, int position) {
        final String name = recipes.get(position).getName();
        vh.tvRecipeName.setText(name);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    private boolean isRecipeChecked(int index) {
        return true;
    }

    public List<Recipe> getCheckedRecipes() {
        int index = 0;
        List<Recipe> checkedRecipes = new LinkedList<Recipe>();
        for(Recipe recipe : this.recipes) {
            if(isRecipeChecked(index)) {
                checkedRecipes.add(recipe);
            }
            index++;
        }

        return checkedRecipes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CheckedTextView tvRecipeName;
        public View rowLayout;

        public ViewHolder(View v) {
            super(v);
            rowLayout = v;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tvRecipeName.isChecked()) {
                        tvRecipeName.setChecked(false);
                        tvRecipeName.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                    } else {
                        tvRecipeName.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                        tvRecipeName.setChecked(true);
                    }
                }
            });
            tvRecipeName = rowLayout.findViewById(R.id.checkedTextView);
            tvRecipeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tvRecipeName.isChecked()) {
                        tvRecipeName.setChecked(false);
                        tvRecipeName.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                    } else {
                        tvRecipeName.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                        tvRecipeName.setChecked(true);
                    }
                }
            });
        }
    }

    public CreateRecipeBookListAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
