package com.hagenberg.needy.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hagenberg.needy.R;

import java.util.List;

/**
 * Created by thomasmaier on 08.05.18.
 */

public class CreateRecipeBookListAdapter extends RecyclerView.Adapter<CreateRecipeBookListAdapter.ViewHolder> {
    private List<String> recipes;   //Changes to generic type recipes as soon as the entity exists

    @Override
    public CreateRecipeBookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.create_recipe_book_row, parent, false);
        ViewHolder vh = new ViewHolder(view);   //Creates the list item views for the recipes
        return vh;
    }

    //Reduzieren auf ausschließlich Checkbox, Tv durch Text von Checkbox ersetzen (!!!)
    @Override
    public void onBindViewHolder(CreateRecipeBookListAdapter.ViewHolder vh, int position) {
        final String name = recipes.get(position);  //Some implementation for recipes here
        vh.tvRecipeName.setText(name);

        vh.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do some Intent stuff here maybe
            }
        });
        vh.cbRecipeChoosen.setActivated(false);
        vh.cbRecipeChoosen.setText(" ");
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvRecipeName;
        public CheckBox cbRecipeChoosen;
        public View rowLayout;

        public ViewHolder(View v) {
            super(v);
            rowLayout = v;
            tvRecipeName = rowLayout.findViewById(R.id.create_recipe_book_row_tv);
            cbRecipeChoosen = rowLayout.findViewById((R.id.create_recipe_book_row_checkbox));
        }
    }

    public CreateRecipeBookListAdapter(List<String> recipes) {
        this.recipes = recipes;
    }
}
