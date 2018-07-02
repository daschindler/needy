package com.hagenberg.needy.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hagenberg.needy.Activities.CreateRecipeActivity;
import com.hagenberg.needy.Activities.ViewRecipeActivity;
import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.TypeConverters.ColorTypeConverters;
import com.hagenberg.needy.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter for recyclerview which displays all Recipes.
 */
public class ShowAllRecipesListAdapter extends RecyclerView.Adapter<ShowAllRecipesListAdapter.ViewHolder> {
    private List<Recipe> recipes;
    Context context;

    /**
     * Called when data for adapter should be updated. Sets adapter recipes to the parameter value.
     * @param recipes
     * @return true if update was successful, false otherwise.
     */
    public boolean updateData(List<Recipe> recipes){
        if(recipes!=null) {
            this.recipes = recipes;
            return true;
        }
        return false;
    }

    /**
     * Constructor that initializes the adapter with a list of recipes and a context which the adapter needs.
     * @param recipes
     * @param context
     */
    public ShowAllRecipesListAdapter(List<Recipe> recipes, Context context) {
        this.context = context;
        this.recipes = recipes;
    }

    /**
     * Called when ViewHolder is created, to inflate the correct list item layout. Then initializes the viewholder and returns it.
     * @param parent View which is used for inflating the list item.
     * @param i position of the view in the recyclerview list.
     * @return ViewHolder holding the views of the list item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_found_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    /**
     * Called when data is set for the recyclerview. Initializes the views of the ViewHolder with the values of the recipe for the given position i.
     * Also sets onClickListeners that lead to the detailView of the recipes, and sets icons for the recipes based on the name of them.
     * @param viewHolder holds the current list items view.
     * @param i position of the list item view in the whole recyclerview.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Recipe recipe = recipes.get(i);
        viewHolder.tvRecipeName.setText(recipe.getName());

        String ingredientsString = "";

        List<Ingredient> ingredients = recipe.getIngredients();
        if(ingredients != null){
            for(Ingredient ing : ingredients){
                if(ingredientsString != ""){
                    ingredientsString+=", ";
                }

                ingredientsString += ing.getName();
            }
        }

        if(ingredientsString == ""){
            ingredientsString = "Just air.";
        }

        Log.d("Recipe with color:", ColorTypeConverters.someObjectListToString(recipe.getColor()));

        viewHolder.tvIngredients.setText(ingredientsString);

        ingredientsString = ingredientsString.toLowerCase();
        String lowerName = recipe.getName().toLowerCase();

        viewHolder.cvDrinkIcon.setImageDrawable(context.getDrawable(R.drawable.cocktail));

        if(ingredientsString.contains("beer") || lowerName.contains("beer")){
            viewHolder.cvDrinkIcon.setImageDrawable(context.getDrawable(R.drawable.beer));
        }else if(ingredientsString.contains("wine") || lowerName.contains("wine")){
            viewHolder.cvDrinkIcon.setImageDrawable(context.getDrawable(R.drawable.wine));
        }else if(ingredientsString.contains("hot") || lowerName.contains("hot")
                || ingredientsString.contains("coffee") || lowerName.contains("coffee")){
            viewHolder.cvDrinkIcon.setImageDrawable(context.getDrawable(R.drawable.hotcoffee));
        }else if(ingredientsString.contains("gin") || lowerName.contains("gin")){
            viewHolder.cvDrinkIcon.setImageDrawable(context.getDrawable(R.drawable.gin));
        }

        viewHolder.tvRecipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call detail view of clicked Recipe.
                Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);
                intent.putExtra("id", recipes.get(i).getUid());
                view.getContext().startActivity(intent);
            }
        });
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);
                intent.putExtra("id", recipes.get(i).getUid());
                view.getContext().startActivity(intent);
            }
        });

    }

    /**
     * Returns the item size of the recipe list.
     * @return list size.
     */
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    /**
     * ViewHolder Class that initializes all views needed for a list item to display recipes.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRecipeName; //recipeName
        public TextView tvIngredients; //ingredients
        public CircleImageView cvDrinkIcon; //drink_icon_view

        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            tvRecipeName = v.findViewById(R.id.recipeName);
            tvIngredients = v.findViewById(R.id.ingredients);
            cvDrinkIcon = v.findViewById(R.id.drink_icon_view);
        }
    }
}
