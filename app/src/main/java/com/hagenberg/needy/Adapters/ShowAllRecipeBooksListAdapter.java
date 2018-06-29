package com.hagenberg.needy.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Activities.ViewRecipeBookActivity;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;
import com.hagenberg.needy.R;

import java.util.LinkedList;
import java.util.List;

public class ShowAllRecipeBooksListAdapter extends RecyclerView.Adapter<ShowAllRecipeBooksListAdapter.ViewHolder> {
    List<RecipeBook> recipeBooks;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recipe_book_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tvRecipeName.setText(recipeBooks.get(i).getName());
        viewHolder.tvRecipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewRecipeBookActivity.class);
                intent.putExtra("id", recipeBooks.get(i).getUid());
                view.getContext().startActivity(intent);
            }
        });
        viewHolder.tvRecipes.setText(getRecipeText(recipeBooks.get(i)));
        viewHolder.ivIcon.setImageResource(R.drawable.ic_cocktail_book);
        viewHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewRecipeBookActivity.class);
                intent.putExtra("id", recipeBooks.get(i).getUid());
                view.getContext().startActivity(intent);
            }
        });
    }

    private String getRecipeText(RecipeBook recipeBook) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for(Recipe rec : recipeBook.getRecipies()) {
            if(i < 3) {
                result.append(rec.getName());
                result.append(", ");
                i++;
            }
        }
        if(i==3) {
            result.append("...");
        } else if (i < 3) {
            result.delete(result.length()-2, result.length());
        }
        return result.toString();

    }

    @Override
    public int getItemCount() {
        return recipeBooks.size();
    }

    public void updateData(List<RecipeBook> recipeBooks) {
        this.recipeBooks = recipeBooks;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRecipeName;
        public View rowLayout;
        public TextView tvRecipes;
        public ImageView ivIcon;

        public ViewHolder(View v) {
            super(v);
            rowLayout = v;
            tvRecipeName = v.findViewById(R.id.tv_rb_name);
            tvRecipes = v.findViewById(R.id.tv_rb_recipes);
            ivIcon = v.findViewById(R.id.iv_rb_icon);
        }
    }

    public ShowAllRecipeBooksListAdapter(List<RecipeBook> recipeBooks) {
        this.recipeBooks = recipeBooks;
    }
}
