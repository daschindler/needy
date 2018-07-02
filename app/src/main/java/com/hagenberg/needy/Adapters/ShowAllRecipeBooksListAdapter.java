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

/**
 * ListAdapter for RecyclerView that shows all Recipe Books.
 */
public class ShowAllRecipeBooksListAdapter extends RecyclerView.Adapter<ShowAllRecipeBooksListAdapter.ViewHolder> {
    List<RecipeBook> recipeBooks;

    /**
     * Called on viewHolder creation, inflates layout.
     * @param viewGroup View which we inflate from.
     * @param i position of the view in the list.
     * @return ViewHolder that holds the views of the list item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recipe_book_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Called when data for the adapter is set, sets the views of each list item to the correct values from their recipe book.
     * Also sets the onClickListeners to show the detail of the recipebook if clicked.
     * @param viewHolder ViewHolder that holds current view that needs to get its data set.
     * @param i position of the view in the list.
     */
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

    /**
     * Converts the list of recipes from a recipebook to a string which can be displayed in the list items cardView.
     * @param recipeBook Book for which the recipes should be converted to a string.
     * @return A string with the text of the recipes for the given recipebook.
     */
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

    /**
     * Returns with the size of the recipebooks in the list, hence the list size.
     * @return size of the recipeBooks list.
     */
    @Override
    public int getItemCount() {
        return recipeBooks.size();
    }

    /**
     * Called to change the data if new recipebooks were added or old ones deleted.
     * @param recipeBooks new set of recipebooks.
     */
    public void updateData(List<RecipeBook> recipeBooks) {
        this.recipeBooks = recipeBooks;
    }

    /**
     * ViewHolder class that initializes the views of the list item layout.
     */
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

    /**
     * Constructor that sets the recipebooks for the adapter.
     * @param recipeBooks
     */
    public ShowAllRecipeBooksListAdapter(List<RecipeBook> recipeBooks) {
        this.recipeBooks = recipeBooks;
    }
}
