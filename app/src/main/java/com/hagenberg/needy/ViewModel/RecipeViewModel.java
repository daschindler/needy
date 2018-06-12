package com.hagenberg.needy.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hagenberg.needy.Database.Repositories.NeedyRepository;
import com.hagenberg.needy.Entity.Recipe;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private NeedyRepository mRepository;
    private LiveData<List<Recipe>> mAllRecipes;

    public RecipeViewModel (Application application) {
        super(application);

        mRepository = new NeedyRepository(application);
        mAllRecipes = mRepository.getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() { return mAllRecipes; }

    public void insert(Recipe recipe) { mRepository.insert(recipe); }

    public void update(Recipe recipe) { mRepository.update(recipe); }
}
