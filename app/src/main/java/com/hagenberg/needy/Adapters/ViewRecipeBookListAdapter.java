package com.hagenberg.needy.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hagenberg.needy.Activities.ViewRecipeActivity;
import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.R;

import java.util.List;

public class ViewRecipeBookListAdapter extends RecyclerView.Adapter<ViewRecipeBookListAdapter.ViewHolder> {
    List<Recipe> recipes;

    public ViewRecipeBookListAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row = inflater.inflate(R.layout.recipe_found_list_item, viewGroup, false);
        ViewHolder rowVH = new ViewHolder(row);
        return rowVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvName.setText(recipes.get(i).getName());
        viewHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);
                intent.putExtra("id", recipes.get(i).getUid());
                view.getContext().startActivity(intent);
            }
        });
        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);
                intent.putExtra("id", recipes.get(i).getUid());
                view.getContext().startActivity(intent);
            }
        });

        String ingredientsString = getIngredients(recipes.get(i).getIngredients());

        viewHolder.tvIngredients.setText(ingredientsString);

        ingredientsString = ingredientsString.toLowerCase();
        String lowerName = recipes.get(i).getName().toLowerCase();

        viewHolder.ivIcon.setImageDrawable(viewHolder.ivIcon.getContext().getDrawable(R.drawable.cocktail));

        if(ingredientsString.contains("beer") || lowerName.contains("beer")){
            viewHolder.ivIcon.setImageDrawable(viewHolder.ivIcon.getContext().getDrawable(R.drawable.beer));
        }else if(ingredientsString.contains("wine") || lowerName.contains("wine")){
            viewHolder.ivIcon.setImageDrawable(viewHolder.ivIcon.getContext().getDrawable(R.drawable.wine));
        }else if(ingredientsString.contains("hot") || lowerName.contains("hot")
                || ingredientsString.contains("coffee") || lowerName.contains("coffee")){
            viewHolder.ivIcon.setImageDrawable(viewHolder.ivIcon.getContext().getDrawable(R.drawable.hotcoffee));
        }else if(ingredientsString.contains("gin") || lowerName.contains("gin")){
            viewHolder.ivIcon.setImageDrawable(viewHolder.ivIcon.getContext().getDrawable(R.drawable.gin));
        }
    }

    private String getIngredients(List<Ingredient> ingredients) {
        String ingredientsString = "" ;

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
        return ingredientsString;
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public View rowLayout;
        public TextView tvIngredients;
        public ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowLayout = itemView;
            tvName = itemView.findViewById(R.id.recipeName);
            tvIngredients = itemView.findViewById(R.id.ingredients);
            ivIcon = itemView.findViewById(R.id.drink_icon_view);

        }
    }
}
