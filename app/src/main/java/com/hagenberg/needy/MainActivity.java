package com.hagenberg.needy;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hallo Leute, hier eine kleine Demo wie ihr mit Dominiks cooler Datenbank interagieren könnt

        //Zuerst holt ihr euch das ViewModel (zB das für Recipe)
        RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        //ViewModel Anfrage: zB Alle Rezepte Getten (als LIVEDATA omg :o)
        LiveData<List<Recipe>> allRecipes = recipeViewModel.getAllRecipes();

        //So bekommt ihr die Daten aus dem LiveData-Holder
        List<Recipe> recipesOutsideOfLiveData = allRecipes.getValue();

        //Regestriert euch beim Live-Data Objekt für einen Callback bei Änderungen in der Datenbank
        allRecipes.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable final List<Recipe> recipes) {
                // allRecipes hat sich geändert (new entry, deleted entry, async insert, ...)
                if(recipes != null){
                    for(Recipe r : recipes){
                        Log.d("Stored Recipe", r.getName());
                    }
                }
            }
        });

        //Hier mal ein neues Beispiels Rezept:
        Recipe sampleRecipe1 = new Recipe("sample 1", "Desc fuer sample1", null);

        //Das jetzt so in die Datenbank gespeichert wird:
        recipeViewModel.insert(sampleRecipe1);

        //Das ganze passiert async um die UI nicht zu freezen
        //Wenn der Insert erfolgreich war, gibt euch euer Observer oben bescheid
    }
}
