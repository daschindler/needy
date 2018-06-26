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

    public RecipeBookViewModel(Application application) {
        super(application);
        mRepository = new NeedyRepository(application);
    }

    // - - -  Getting Data - - -
    public LiveData<List<RecipeBook>> getAllLiveRecipeBooks() { return mRepository.getAllRecipeBooks(); }

    public LiveData<RecipeBook> getRecipeBookById(int id) {
        return mRepository.getRecipeBookById(id);
    }

    public LiveData<List<RecipeBook>> getRecipeBooksById(int... id) {
        return mRepository.getRecipeBooksByIds(id);
    }

    public LiveData<List<RecipeBook>> getAllLiveRecipeBooksByNameOrDesc(String keyword) { return mRepository.getRecipeBooksByNameOrDesc(keyword); }




    // - - - Inserts & Updates - - -

    public void insert(RecipeBook... recipebooks) { mRepository.insert(recipebooks); }

    public void update(RecipeBook... recipebook) { mRepository.insert(recipebook); }

    public void delete(RecipeBook... recipebook) { mRepository.delete(recipebook); }
}
