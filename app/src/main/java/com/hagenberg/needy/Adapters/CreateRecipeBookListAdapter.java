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
 * ListAdapter for RecyclerView
 * Organizes recipes in CreateRB-View, for displaying recipes with checkbox.
 */
public class CreateRecipeBookListAdapter extends RecyclerView.Adapter<CreateRecipeBookListAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private Boolean[] isChecked;

    /**
     * Updates the listdata with the recipe parameter, and sets everything unchecked.
     * @param recipes List of the new recipes.
     */
    public void updateData(List<Recipe> recipes) {
        this.recipes = recipes;
        this.isChecked = setUnchecked(new Boolean[recipes.size()]);

    }

    /**
     * Updates the listdata with new recipes, and sets everything in the given array to checked if true.
     * @param recipes List of the new recipes.
     * @param checked Holds values for each recipe, true ==> recipe is checked.
     */
    public void updateData(List<Recipe> recipes, Boolean[] checked) {
        if(recipes.size()==checked.length) {
            this.recipes = recipes;
            this.isChecked = checked;
        }
    }

    /**
     * Sets all checked values in the array to unchecked
     * @param recipes array with the correct length, which will be set to false.
     * @return Returns an Array with the size of recipes-list, where all values are set to false.
     */
    private Boolean[] setUnchecked(Boolean[] recipes) {
        for (int i = 0; i<recipes.length; i++){
            recipes[i] = Boolean.FALSE;
        }
        return recipes;
    }

    /**
     * Called on creating the view, creates the ViewHolder.
     * @param parent View from which we inflate.
     * @param viewType not used.
     * @return The ViewHolder-Class holding the inflated views.
     */
    @Override
    public CreateRecipeBookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.create_recipe_book_row, parent, false);
        ViewHolder vh = new ViewHolder(view);   //Creates the list item views for the recipes
        return vh;
    }

    /**
     * Called when data is set for the adapter. Sets the recipe data for the list item, and the listeners for clicking the checkboxes.
     * @param vh ViewHolder to set the data for
     * @param position Position in the list
     */
    @Override
    public void onBindViewHolder(final CreateRecipeBookListAdapter.ViewHolder vh, final int position) {
        final String name = recipes.get(position).getName();
        vh.tvRecipeName.setText("   " + name);
        if(isChecked!=null) {
            if (isChecked[position]) {
                vh.tvRecipeName.setCheckMarkDrawable(R.drawable.ic_checkbox_on);
            } else {
                vh.tvRecipeName.setCheckMarkDrawable(R.drawable.ic_checkbox_off);
            }
            vh.tvRecipeName.setChecked(isChecked[position]);
        }
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

    /**
     * Returns size of list.
     * @return size of list.
     */
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    /**
     * Checks  if the list item for the given index is checked or not.
     * @param index for which the list item should be looked up.
     * @return true if recipe is checked in the views, and false if recipe isn't checked.
     */
    private boolean isRecipeChecked(int index) {
        return this.isChecked[index];
    }

    /**
     * Returns all recipes in the list that are currently checked.
     * @return
     */
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

    /**
     * ViewHolder class for this adapter.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckedTextView tvRecipeName;
        public View rowLayout;

        public ViewHolder(View v) {
            super(v);
            rowLayout = v;
            tvRecipeName = rowLayout.findViewById(R.id.checkedTextView);
        }
    }

    /**
     * Constructor that initializes the recipes.
     * @param recipes new recipe list.
     */
    public CreateRecipeBookListAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
        this.isChecked = setUnchecked(new Boolean[recipes.size()]);
    }

    /**
     * Constructor that initializes the recipes, and sets everything in the list to the checked-value in the given array.
     * @param recipes new recipe list.
     * @param isChecked array with checked values for the recipe list.
     */
    public CreateRecipeBookListAdapter(List<Recipe> recipes, Boolean[] isChecked) {
        this.recipes = recipes;
        this.isChecked = isChecked;
    }
}
