package com.hagenberg.needy.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.Unit;
import com.hagenberg.needy.R;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportRecipeActivity extends AppCompatActivity {

    private LinearLayout ll_explorer;
    private Button bt_level_up;

    private File actPathFile;
    private int stageCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ll_explorer = findViewById(R.id.ll_import_recipe_explorer);
        bt_level_up = findViewById(R.id.bt_import_recipe_folder_level_up);

        bt_level_up.setVisibility(View.GONE);

        final File sdcard = Environment.getExternalStorageDirectory();

        SetupFilebrowser(sdcard);

        bt_level_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stageCounter = stageCounter - 1;

                if (stageCounter == 0) {
                    bt_level_up.setVisibility(View.GONE);
                }
                ll_explorer.removeAllViews();
                SetupFilebrowser(actPathFile.getParentFile());
            }
        });


    }

    //Aufruf des Setups für mitgegebene Ordner"ebene"
    private void SetupFilebrowser(File filepath) {
        actPathFile = filepath;
        File dirs = new File(filepath.getAbsolutePath());

        if(dirs.exists()) {
            final File[] files = dirs.listFiles();
            for (int i = 0; i < files.length; i++){
                //Layout für einzelnen Ordner/File vorbereiten
                final LinearLayout llProgExplorer = new LinearLayout(this);
                llProgExplorer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 160));
                llProgExplorer.setPadding(70,60,100,10);
                llProgExplorer.setOrientation(LinearLayout.HORIZONTAL);
                ImageView imgvType = new ImageView(this);
                //Setup wenn es sich um einen Ordner handelt
                if (files[i].isDirectory()){
                    if (!files[i].getName().startsWith(".") && !files[i].getName().contains(".")) {
                        TextView tvFoldername = new TextView(this);

                        tvFoldername.setText(files[i].getName());
                        tvFoldername.setId(i+1);
                        tvFoldername.setTextSize(21);
                        imgvType.setImageResource(R.drawable.ic_folder_grey_24dp);
                        imgvType.setId(i+2);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        params.weight = 1.0f;
                        tvFoldername.setLayoutParams(params);

                        llProgExplorer.setId(i);
                        llProgExplorer.addView(tvFoldername);
                        llProgExplorer.addView(imgvType);


                        ll_explorer.addView(llProgExplorer);

                        //Methode wird erneut mit "tieferem" Pfad aufgerufen
                        llProgExplorer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (stageCounter == 0) {
                                    bt_level_up.setVisibility(View.VISIBLE);
                                }
                                stageCounter = stageCounter + 1;
                                int id = llProgExplorer.getId();
                                ll_explorer.removeAllViews();
                                SetupFilebrowser(files[id]);
                            }
                        });
                    }
                    //Setup wenn es sich um eine Datei handelt
                } else if (files[i].isFile()){
                    if (files[i].getName().contains(".needy") && !files[i].getName().startsWith(".")){
                        TextView tvFilename = new TextView(this);
                        tvFilename.setText(files[i].getName());
                        tvFilename.setId(i+1);
                        tvFilename.setTextSize(21);
                        llProgExplorer.setId(i);
                        imgvType.setImageResource(R.drawable.ic_insert_drive_file_grey_24dp);
                        imgvType.setId(i+2);
                        tvFilename.setTextColor(getResources().getColor(R.color.colorPrimary));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        params.weight = 1.0f;
                        tvFilename.setLayoutParams(params);

                        llProgExplorer.addView(tvFilename);
                        llProgExplorer.addView(imgvType);
                        ll_explorer.addView(llProgExplorer);

                        llProgExplorer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            //Pfad wird an andere Activity mitgegeben
                            public void onClick(View view) {
                                StoreRecipe(files[llProgExplorer.getId()]);
                                ImportRecipeActivity.this.finish();
                            }
                        });

                    }
                }
            }
        }
    }

    private void StoreRecipe(File file) {
        StringBuilder recipeFromFileString = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                recipeFromFileString.append(line);
            }
            br.close();
        }
        catch (IOException e) {
            Log.e("readRecipeFromFile", e.getMessage());
        }

        Log.d("recipefromfilestring", recipeFromFileString.toString());

        String[] recipeArry = recipeFromFileString.toString().split(";");

        String recipeName = recipeArry[0];
        String recipeDesc = recipeArry[1];
        List<Ingredient> ingredientList = new ArrayList<>();

        for (int i = 2; i <recipeArry.length; i++){
            String[] ingredientArry = recipeArry[i].split("/");
            String ingredientName = ingredientArry[0];
            int ingredientAmount = Integer.valueOf(ingredientArry[1]);
            Unit ingredientUnit = Unit.valueOf(ingredientArry[2]);

            ingredientList.add(new Ingredient(ingredientName, ingredientAmount, ingredientUnit));

        }

        final Recipe recipeToStore = new Recipe(recipeName, recipeDesc, ingredientList);
        RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        recipeViewModel.insert(recipeToStore);
    }
}
