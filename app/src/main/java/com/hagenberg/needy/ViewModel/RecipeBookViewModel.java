package com.hagenberg.needy.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hagenberg.needy.Database.Repositories.NeedyRepository;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;

import java.util.List;

public class RecipeBookViewModel extends AndroidViewModel {

    private NeedyRepository mRepository;
    private LiveData<List<RecipeBook>> mAllRecipeBooks;

    public RecipeBookViewModel(Application application) {
        super(application);

        mRepository = new NeedyRepository(application);
        mAllRecipeBooks = mRepository.getAllRecipeBooks();
    }

    public LiveData<List<RecipeBook>> getAllRecipes() { return mAllRecipeBooks; }

    public void insert(RecipeBook recipebook) { mRepository.insert(recipebook); }

    public void update(RecipeBook recipebook) { mRepository.insert(recipebook); }
}
