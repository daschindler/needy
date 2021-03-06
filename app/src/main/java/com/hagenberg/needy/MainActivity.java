package com.hagenberg.needy;

import android.Manifest;
import android.app.SearchManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hagenberg.needy.Adapters.MainPagerAdapter;
import com.hagenberg.needy.Entity.Color;
import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.Unit;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * LauncherActivity, sets up ViewPager.
 */
public class MainActivity extends AppCompatActivity {

    MainPagerAdapter viewPagerAdapter;
    ViewPager viewPager;

    /**
     * Called after the startup.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ShowDatabaseFunctionality();
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);

        viewPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);

        viewPagerAdapter.setSearchString("");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);
        handleIntent(getIntent());

        AskStoragePermissions();
    }

    //region Database Demo
    //Demo wie man mit Dominiks cooler Datenbank interagieren kann
    public void ShowDatabaseFunctionality() {
        //Beispiel: im OnCreate von einer Activity, die ein Rezept mit ID 3 anzeigt

        int id = 3;

        //Passendes ViewModel holen:
        final RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        //Views initialisieren
        //textView = findViewById ...

        //Warum das zuerst?
        // - Es passieren im Anschluss zwei Tasks in zwei Threads gleichzeitig
        // Das laden der Daten (Data-Thread) und das Vorbereiten der Views (UI-Thread)
        // Wenn der Data-Thread vor dem UI-Thread abgeschlossen ist, gibt es einen Callback zu dem
        // Observer (weiter unten). Der schreibt die Daten in die Views! Wenn die Views aber noch nicht
        // initialisiert wurden bis zu dem Zeitpunkt, gibt es eine Nullpointer Exception

        //Gedankengang:
            // glaube es wäre noch effektiver, die daten zuerst zu laden und danach das initislisieren der views zu starten
            // Dafür bräuchte man im Observer noch eine Abfrage ob die Views schon initialisiert sind
            // -> letzt-geladene-view != null

            // Und nach dem initialisieren der Views eine Abfrage, ob Daten schon geladen wurden
            // -> zB observer setzt lokale Recipe Variable nach view-initisliasiern checkt man ob diese schon befüllt ist)

        //Gesuchte Daten als LIVE DATA holen:
        LiveData<Recipe> recipeById = recipeViewModel.getRecipeById(3);

        //Live-Data "beobachten" (observe)
        recipeById.observe(this, new Observer<Recipe>() {
            boolean isLoaded = false;

            @Override
            public void onChanged(@Nullable final Recipe recipe) {
                if(recipe != null){
                    Log.d("Recipe with ID 3", recipe.getName());
                    //view.settext(recipe. ...)
                }else{
                    //this recipe got deleted
                }
            }
        });

        //DAS WARS!

        //Altes Beispiel mit AllRecipes:

        //ViewModel Anfrage: zB Alle Rezepte Getten
        LiveData<List<Recipe>> allRecipes = recipeViewModel.getAllRecipes();


        //Regestriert euch beim Live-Data Objekt für einen Callback wenns geladen ist oder der Eintrag sich ändert
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

        insertTestRecipes(recipeViewModel);
    }

    public void insertTestRecipes(RecipeViewModel recipeViewModel){
        //In Datenbank inserten:

        Color c;
        Color c2;
        Color c3;
        Color c4;

        Random r = new Random();
        int randomAmount = r.nextInt(3);
        Ingredient i = new Ingredient("salz", randomAmount, Unit.CoffeeSpoon);
        randomAmount = r.nextInt(3);
        Ingredient i2 = new Ingredient("vodka", randomAmount, Unit.Liter);
        randomAmount = r.nextInt(3);
        Ingredient i3 = new Ingredient("water", randomAmount, Unit.Liter);

        switch (randomAmount){
            case 1:
                c4 = Color.BLUE;
            case 2:
                c4 = Color.GREEN;
            default:
                c4 = Color.RED;
        }

        randomAmount = r.nextInt(3);
        Ingredient i4 = new Ingredient("coffee", randomAmount, Unit.Liter);

        switch (randomAmount){
            case 1:
                c3 = Color.BLUE;
            case 2:
                c3 = Color.GREEN;
            default:
                c3 = Color.RED;
        }

        randomAmount = r.nextInt(3);
        Ingredient i5 = new Ingredient("beer", randomAmount, Unit.Liter);

        switch (randomAmount){
            case 1:
                c = Color.BLUE;
            case 2:
                c = Color.GREEN;
            default:
                c = Color.RED;
        }

        randomAmount = r.nextInt(3);
        Ingredient i6 = new Ingredient("gin", randomAmount, Unit.Liter);

        switch (randomAmount){
            case 1:
                c2 = Color.BLUE;
            case 2:
                c2 = Color.GREEN;
            default:
                c2 = Color.RED;
        }

        LinkedList<Ingredient> ings = new LinkedList<>();
        ings.add(i); ings.add(i2); ings.add(i3);

        Recipe sampleRecipe1 = new Recipe("Salty Water", "Desc fuer sample1", ings);

        ings = new LinkedList<>();
        ings.add(i2); ings.add(i4);

        Recipe sampleRecipe2 = new Recipe("Hot Schwips", "Desc fuer sample1", ings);

        ings = new LinkedList<>();
        ings.add(i5);

        Recipe sampleRecipe3 = new Recipe("Just Beer", "Desc fuer sample1", ings);

        ings = new LinkedList<>();
        ings.add(i6); ings.add(i2);

        Recipe sampleRecipe4 = new Recipe("Somethin Ginish", "Desc fuer sample1", ings);

        sampleRecipe1.setColor(c);
        sampleRecipe2.setColor(c2);
        sampleRecipe3.setColor(c3);
        sampleRecipe4.setColor(c4);

        recipeViewModel.insert(sampleRecipe1);
        recipeViewModel.insert(sampleRecipe2);
        recipeViewModel.insert(sampleRecipe3);
        recipeViewModel.insert(sampleRecipe4);
    }

    //endregion

    private void AskStoragePermissions(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
    }

    /**
     * Called after initializing the activity. Inflates the menu.
     * @param menu menu to inflate.
     * @return handled by super class.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater  = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        //Set up searchview
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.main_menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //Live search for each text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                viewPagerAdapter.setSearchString(query);
                viewPagerAdapter.notifyDataSetChanged();
                return true;
            }
        });

        //On opening the search view, the fragments get set an empty search string.
        menu.findItem(R.id.main_menu_search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                viewPagerAdapter.setSearchString("");
                viewPagerAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles queries of the searchable view.
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * Handles a search query, and sets it for the displayed fragments of the ViewPagerAdapter.
     * @param intent calling intent. 
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            viewPagerAdapter.setSearchString(query);
            viewPagerAdapter.notifyDataSetChanged();
        }
    }
}
