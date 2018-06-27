package com.hagenberg.needy.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.R;

import java.util.LinkedList;
import java.util.List;

public class ShowAllIngredientsListAdapter extends RecyclerView.Adapter<ShowAllIngredientsListAdapter.ViewHolder> {
    private List<Recipe> recipes;

    private List<String> allIngredients = new LinkedList<String>();
    private List<String> selectedIngredients = new LinkedList<String>();

    private ShowFoundRecipesByIngredientsListAdapter recipeAdapter;

    public ShowAllIngredientsListAdapter(List<Recipe> recipes, ShowFoundRecipesByIngredientsListAdapter recipeAdapter) {
        this.recipeAdapter = recipeAdapter;

        updateData(recipes);
    }

    public boolean updateData(List<Recipe> recipes){
        if (recipes == null){
            this.recipes = new LinkedList<Recipe>();
            recipeAdapter.updateIngredients(this.selectedIngredients);
            return false;
        }

        if(recipes.size() > 0) {
            this.recipes = recipes;

            this.allIngredients = new LinkedList<String>();

            for (Recipe r : recipes){
                if(r != null){
                    if(r.getIngredients() != null){
                        for(Ingredient i : r.getIngredients()){
                            if(!allIngredients.contains(i.getName())){
                                this.allIngredients.add(i.getName().toLowerCase());
                            }
                        }
                    }
                }
            }

            recipeAdapter.updateIngredients(this.selectedIngredients);
            return true;
        }

        recipeAdapter.updateIngredients(this.selectedIngredients);
        return false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.ingredients_list_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final String ingredientName = this.allIngredients.get(i);
        viewHolder.tvIngredientName.setText(ingredientName);

        if(selectedIngredients.contains(ingredientName)){
            viewHolder.cvIngredientHolder.setCardBackgroundColor(Color.DKGRAY);
            viewHolder.tvIngredientName.setTextColor(Color.WHITE);
        }else{
            viewHolder.cvIngredientHolder.setCardBackgroundColor(Color.WHITE);
            viewHolder.tvIngredientName.setTextColor(Color.BLACK);
        }

        viewHolder.cvIngredientHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedIngredients.contains(ingredientName)){
                    viewHolder.cvIngredientHolder.setCardBackgroundColor(Color.WHITE);
                    viewHolder.tvIngredientName.setTextColor(Color.BLACK);
                    selectedIngredients.remove(ingredientName);
                }else{
                    viewHolder.cvIngredientHolder.setCardBackgroundColor(Color.DKGRAY);
                    viewHolder.tvIngredientName.setTextColor(Color.WHITE);
                    selectedIngredients.add(ingredientName);
                }

                recipeAdapter.updateIngredients(selectedIngredients);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIngredientName;
        public CardView cvIngredientHolder;

        public ViewHolder(View v) {
            super(v);
            tvIngredientName = v.findViewById(R.id.IngredientName);
            cvIngredientHolder = v.findViewById(R.id.ingredient_holder);
        }

    }
}
