package com.hagenberg.needy.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hagenberg.needy.Database.Repositories.NeedyRepository;
import com.hagenberg.needy.Entity.Recipe;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private NeedyRepository mRepository;

    public RecipeViewModel (Application application) {
        super(application);

        mRepository = new NeedyRepository(application);
    }

    // - - -  Getting Live Data - - -
    public LiveData<List<Recipe>> getAllLiveRecipes() { return mRepository.getAllRecipes(); }

    public LiveData<List<Recipe>> getAllLiveRecipesByNameOrDesc(String keyword) { return mRepository.getRecipesByNameOrDesc(keyword); }


    // - - - Getting Data NOT Live - - -
    public List<Recipe> getAllCurrentRecipes() {
        return mRepository.getAllCurrentRecipes();
    }

    public List<Recipe> getAllCurrentRecipesByNameOrDesc(String keyword) {

        LiveData<List<Recipe>> recipeList = mRepository.getRecipesByNameOrDesc(keyword);

        if(recipeList == null){
            return null;
        }

        return recipeList.getValue();

    }

    public Recipe getCurrentRecipeById(int id) {
        return mRepository.getRecipeById(id);
    }

    public List<Recipe> getCurrentRecipesById(int... id) {
        return mRepository.getRecipesByIds(id);
    }


    // - - - Inserts & Updates - - -

    public void insert(Recipe... recipe) { mRepository.insert(recipe); }

    public void update(Recipe recipe) { mRepository.update(recipe); }
}
