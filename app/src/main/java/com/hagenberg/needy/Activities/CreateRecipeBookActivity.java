package com.hagenberg.needy.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.hagenberg.needy.Adapters.CreateRecipeBookListAdapter;
import com.hagenberg.needy.R;
import java.util.ArrayList;
import java.util.List;

public class CreateRecipeBookActivity extends AppCompatActivity {

    Button btFinish;
    Button btCancel;
    EditText etRecipeBookName;
    RecyclerView rvChoosenRecipes;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe_book);

        btCancel = (Button) findViewById(R.id.createRB_bt_cancel);
        btFinish = (Button) findViewById(R.id.createRB_bt_finish);
        etRecipeBookName = (EditText) findViewById(R.id.createRB_et_name);
        rvChoosenRecipes = (RecyclerView) findViewById(R.id.createRB_rv_recipes);

        //RecyclerView Specs + Adapter + Values setzen
        rvChoosenRecipes.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvChoosenRecipes.setLayoutManager(layoutManager);

        //Some recipe values for the adapter, usually taken outta the SQLite Database
        List<String> values = new ArrayList<>();
        values.add("Some Recipes");
        CreateRecipeBookListAdapter adapter = new CreateRecipeBookListAdapter(values);
        rvChoosenRecipes.setAdapter(adapter);
    }
}
