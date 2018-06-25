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
    private Boolean[] isChecked;

    public void updateData(List<Recipe> recipes) {
        this.recipes = recipes;
        this.isChecked = setUnchecked(new Boolean[recipes.size()]);

    }

    private Boolean[] setUnchecked(Boolean[] recipes) {
        for (int i = 0; i<recipes.length; i++){
            recipes[i] = Boolean.FALSE;
        }
        return recipes;
    }

    @Override
    public CreateRecipeBookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.create_recipe_book_row, parent, false);
        ViewHolder vh = new ViewHolder(view);   //Creates the list item views for the recipes
        return vh;
    }

    @Override
    public void onBindViewHolder(final CreateRecipeBookListAdapter.ViewHolder vh, final int position) {
        final String name = recipes.get(position).getName();
        vh.tvRecipeName.setText("   " + name);
        vh.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vh.tvRecipeName.isChecked()) {
                    vh.tvRecipeName.setChecked(false);
                    vh.tvRecipeName.setCheckMarkDrawable(R.drawable.ic_checkbox_off);
                    isChecked[position] = Boolean.FALSE;
                } else {
                    vh.tvRecipeName.setCheckMarkDrawable(R.drawable.ic_checkbox_on);
                    vh.tvRecipeName.setChecked(true);
                    isChecked[position] = Boolean.TRUE;
                }
            }
        });
        vh.tvRecipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vh.tvRecipeName.isChecked()) {
                    vh.tvRecipeName.setChecked(false);
                    vh.tvRecipeName.setCheckMarkDrawable(R.drawable.ic_checkbox_off);
                    isChecked[position] = Boolean.FALSE;
                } else {
                    vh.tvRecipeName.setCheckMarkDrawable(R.drawable.ic_checkbox_on);
                    vh.tvRecipeName.setChecked(true);
                    isChecked[position] = Boolean.TRUE;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    private boolean isRecipeChecked(int index) {
        return this.isChecked[index];
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
            tvRecipeName = rowLayout.findViewById(R.id.checkedTextView);
        }
    }

    public CreateRecipeBookListAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
        this.isChecked = setUnchecked(new Boolean[recipes.size()]);
    }
}
