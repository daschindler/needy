package com.hagenberg.needy.Activities;

import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Adapters.ViewRecipeBookListAdapter;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeBookViewModel;

import java.util.LinkedList;

public class ViewRecipeBookActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvDesc;
    Button btShare;
    Button btEdit;
    RecyclerView rvRecipes;
    RecipeBook publicRB;
    RecipeBookViewModel recipeBookViewModel;
    RecyclerView.LayoutManager layoutManager;
    ViewRecipeBookListAdapter listAdapter = new ViewRecipeBookListAdapter(new LinkedList<Recipe>());
    LiveData<RecipeBook> rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_book);
        tvName = findViewById(R.id.viewRB_tv_name);
        tvDesc = findViewById(R.id.viewRB_tv_desc);
        btEdit = findViewById(R.id.viewRB_bt_edit);
        btShare = findViewById(R.id.viewRB_bt_share);
        rvRecipes = findViewById(R.id.viewRB_rv_recipes);
        recipeBookViewModel = ViewModelProviders.of(this).get(RecipeBookViewModel.class);

        layoutManager = new LinearLayoutManager(this);
        rvRecipes.setLayoutManager(layoutManager);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(publicRB !=null) {
                    Intent intent = new Intent(view.getContext(), CreateRecipeBookActivity.class);
                    intent.putExtra("id", publicRB.getUid());
                    view.getContext().startActivity(intent);
                } else{
                    Toast.makeText(view.getContext(), "Please wait until everything is finished loading...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        int id = 0;
        Intent calling = getIntent();
        Bundle extras = calling.getExtras();

        if(extras != null) {
            id = (int) extras.get("id");
        }
        rb = recipeBookViewModel.getRecipeBookById(id);
        rb.observe(this, new Observer<RecipeBook>() {
            @Override
            public void onChanged(@Nullable RecipeBook recipeBook) {
                if(recipeBook!=null) {
                    publicRB = recipeBook;
                    tvName.setText(recipeBook.getName());
                    tvDesc.setText(recipeBook.getDescription());
                    listAdapter = new ViewRecipeBookListAdapter(recipeBook.getRecipies());
                    rvRecipes.setAdapter(listAdapter);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_recipe_book_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.view_recipe_book_menu_delete:
                deleteRecipeBook();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteRecipeBook() {
        //Start up delete-dialog
        if(rb!=null) {
            final Dialog deleteDialog = new Dialog(this, R.style.DescriptionDialog);
            deleteDialog.setContentView(R.layout.dialog_delete_recipe_book);
            Button btCancel = deleteDialog.findViewById(R.id.dialog_deleteRB_bt_cancel);
            Button btDelete = deleteDialog.findViewById(R.id.dialog_deleteRB_bt_delete);
            TextView tvTitle = deleteDialog.findViewById(R.id.dialog_deleteRB_title);
            TextView tvText = deleteDialog.findViewById(R.id.dialog_deleteRB_text);

            if(rb.getValue()!=null) {
                publicRB = rb.getValue();
                tvTitle.setText(publicRB.getName());
                tvText.setText("Do you really want to delete this recipe book?");
                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteDialog.dismiss();
                    }
                });
                btDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //delete happens
                        onBackPressed();
                    }
                });
            }
            deleteDialog.show();
        }
    }
}
