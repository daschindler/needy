package com.hagenberg.needy.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        View row = inflater.inflate(R.layout.recipe_list_item, viewGroup);
        ViewHolder rowVH = new ViewHolder(row);
        return rowVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvName.setText(recipes.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        View rowLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowLayout = itemView;
            tvName = itemView.findViewById(R.id.textMain);
        }
    }
}
