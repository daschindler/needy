package com.hagenberg.needy.Activities;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Adapters.ViewRecipeBookListAdapter;
import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeBookViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStoragePermission()){
                    CreateFileToShare();
                } else {
                    askStoragePermission();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
                    CreateFileToShare();
                } else {
                    Toast.makeText(this, "We are sorry, but sharing Recipe Books is only possible with storage access...", Toast.LENGTH_LONG).show();
                }
            default:
                return;
        }
    }

    private boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void askStoragePermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
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

    private void CreateFileToShare() {
        try {
            File folder = new File(Environment.getExternalStorageDirectory().toString()+ "/needy");
            if(!folder.isDirectory()) {
                folder.mkdirs();
            }

            File file = new File(Environment.getExternalStorageDirectory().toString(), "/needy/" + publicRB.getName() + ".rbneedy");
            FileOutputStream fileOutput = new FileOutputStream(file);
            OutputStreamWriter streamWriter = new OutputStreamWriter(fileOutput);
            String fileContent = FormatRecipebookToString(publicRB);
            streamWriter.write(fileContent);
            streamWriter.flush();
            fileOutput.getFD().sync();
            streamWriter.close();
            //Toast.makeText(this,"This recipebook is saved in your storage and ready to be shared with your friends!", Toast.LENGTH_LONG).show();
        } catch (IOException ex) {
            Log.d("Write to storage", "Failed");
        }

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory().toString() +
                "/needy/" + publicRB.getName()+ ".rbneedy"));
        startActivity(Intent.createChooser(sharingIntent, "Share Recipe Book via"));
    }

    private String FormatRecipebookToString(RecipeBook recipeBook) {
        StringBuilder formattedRecipeBook = new StringBuilder();

        if(recipeBook.getName() != null) {
            formattedRecipeBook.append(recipeBook.getName()+";");
        } else {
            formattedRecipeBook.append("no recipebook name;");
        }

        if(recipeBook.getDescription()!=null) {
            formattedRecipeBook.append(recipeBook.getDescription()+";");
        } else {
            formattedRecipeBook.append("no recipebook description;");
        }

        if(recipeBook.getRecipies() != null) {
            for (Recipe recipe : recipeBook.getRecipies()){
                formattedRecipeBook.append(FormatRecipeToString(recipe));
            }
        } else {
            formattedRecipeBook.append("no recipes;");
        }

        return formattedRecipeBook.toString();
    }

    private String FormatRecipeToString(Recipe recipe) {
        StringBuilder formattedRecipe = new StringBuilder();

        if (recipe.getName() != null){
            formattedRecipe.append(recipe.getName()+":");
        } else {
            formattedRecipe.append("no name:");
        }

        if (recipe.getDescription() != null){
            formattedRecipe.append(recipe.getDescription()+":");
        } else {
            formattedRecipe.append("no description:");
        }

        if (recipe.getIngredients() != null) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                formattedRecipe.append(ingredient.getName()+"/");
                formattedRecipe.append(ingredient.getAmount()+"/");
                formattedRecipe.append(ingredient.getAmountUnit().toString()+":");
            }
            formattedRecipe.append(";");
        } else {
            formattedRecipe.append("no ingredientname/no ingredientdesc/no unit;");
        }
        return formattedRecipe.toString();
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
                        recipeBookViewModel.delete(publicRB);
                        onBackPressed();
                    }
                });
            }
            deleteDialog.show();
        }
    }
}
