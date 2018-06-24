package com.hagenberg.needy.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        View view = inflater.inflate(R.layout.recipe_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tvRecipeName.setText(recipeBooks.get(i).getName());
        viewHolder.tvRecipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Position Nr. " + i + " clicked!", Toast.LENGTH_SHORT).show();
            }
        });
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

        public ViewHolder(View v) {
            super(v);
            rowLayout = v;
            tvRecipeName = v.findViewById(R.id.textMain);
        }
    }

    public ShowAllRecipeBooksListAdapter(List<RecipeBook> recipeBooks) {
        this.recipeBooks = recipeBooks;
    }
}
