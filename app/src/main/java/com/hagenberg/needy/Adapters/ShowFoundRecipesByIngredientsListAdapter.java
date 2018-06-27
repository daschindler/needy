package com.hagenberg.needy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hagenberg.needy.Activities.ViewRecipeActivity;
import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.TypeConverters.ColorTypeConverters;
import com.hagenberg.needy.R;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowFoundRecipesByIngredientsListAdapter extends RecyclerView.Adapter<ShowFoundRecipesByIngredientsListAdapter.ViewHolder> {
    private List<Recipe> allRecipes;
    private List<Recipe> availableRecipes;
    private List<String> availableIngredients;
    private Context context;

    public void updateRecipies(List<Recipe> allRecipes){
        if(allRecipes == null){
            this.allRecipes = new LinkedList<Recipe>();
        }else{
            this.allRecipes = allRecipes;
        }

        getAvailableRecipes();

        notifyDataSetChanged();
    }

    public void updateIngredients(List<String> availableIngredients){
        this.availableIngredients = availableIngredients;

        getAvailableRecipes();

        notifyDataSetChanged();
    }

    public ShowFoundRecipesByIngredientsListAdapter(Context context, List<Recipe> recipes) {
        this.context = context;

        this.allRecipes = recipes;
        this.availableIngredients = new LinkedList<String>();

        getAvailableRecipes();
    }

    private void getAvailableRecipes(){
        availableRecipes = new LinkedList<Recipe>();

        if(allRecipes != null){
            for(Recipe r : allRecipes){
                boolean makeAble = true;

                if(r != null){
                    List<Ingredient> ings = r.getIngredients();
                        if(ings != null){
                            for(Ingredient ing : ings){
                                if(ing != null) {
                                    if (!availableIngredients.contains(ing.getName().toLowerCase())) {
                                        makeAble = false;
                                    }
                                }
                            }
                        }

                    if(makeAble){
                        availableRecipes.add(r);
                    }

                    }

                }

            }
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_found_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Recipe recipe = availableRecipes.get(i);
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
                intent.putExtra("id", availableRecipes.get(i).getUid());
                view.getContext().startActivity(intent);
            }
        });
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);
                intent.putExtra("id", availableRecipes.get(i).getUid());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return availableRecipes.size();
    }

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
