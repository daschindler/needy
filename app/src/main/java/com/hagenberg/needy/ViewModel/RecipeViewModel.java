package com.hagenberg.needy.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hagenberg.needy.Database.Repositories.NeedyRepository;
import com.hagenberg.needy.Entity.Recipe;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    //okok

    private NeedyRepository mRepository;

    public RecipeViewModel (Application application) {
        super(application);

        mRepository = new NeedyRepository(application);
    }

    // - - -  Getting Data - - -
    public LiveData<List<Recipe>> getAllRecipes() { return mRepository.getAllRecipes(); }

    public LiveData<Recipe> getRecipeById(int id) {
        return mRepository.getRecipeById(id);
    }

    public LiveData<List<Recipe>> getAllRecipesByNameOrDesc(String keyword) { return mRepository.getRecipesByNameOrDesc(keyword); }



    // - - - Inserts & Updates - - -

    public void insert(Recipe... recipe) { mRepository.insert(recipe); }

    public void update(Recipe... recipe) { mRepository.update(recipe); }

    public void delete(Recipe... recipe) { mRepository.delete(recipe); }
}
