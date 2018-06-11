package com.hagenberg.needy;

import android.app.SearchManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Adapters.MainPagerAdapter;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Fragments.ShowAllRecipesFragment;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    MainPagerAdapter viewPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setSearchString("");
        viewPager.setAdapter(viewPagerAdapter);

        handleIntent(getIntent());
    }

    public void ShowDatabaseFunctionality() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater  = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.widget.SearchView searchView = (SearchView) menu.findItem(R.id.main_menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(getApplicationContext(), "Clear button pressed", Toast.LENGTH_SHORT).show();
                viewPagerAdapter.setSearchString("");
                viewPagerAdapter.notifyDataSetChanged();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            viewPagerAdapter.setSearchString(query);
            viewPagerAdapter.notifyDataSetChanged();
        }
    }
}
