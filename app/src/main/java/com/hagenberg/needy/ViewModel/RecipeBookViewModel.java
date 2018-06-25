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

    // - - -  Getting Live Data - - -
    public LiveData<List<RecipeBook>> getAllLiveRecipeBooks() { return mRepository.getAllRecipeBooks(); }

    public LiveData<List<RecipeBook>> getAllLiveRecipeBooksByNameOrDesc(String keyword) { return mRepository.getRecipeBooksByNameOrDesc(keyword); }

    // - - - Getting Data NOT Live - - -
    public List<RecipeBook> getAllCurrentRecipeBooks() {
        return mRepository.getAllCurrentRecipeBooks();
    }

    public List<RecipeBook> getAllCurrentRecipeBooksByNameOrDesc(String keyword) {
        LiveData<List<RecipeBook>> books = mRepository.getRecipeBooksByNameOrDesc(keyword);

        if(books == null)
            return null;

        return mRepository.getRecipeBooksByNameOrDesc(keyword).getValue();
    }

    public RecipeBook getCurrentRecipeBookById(int id) {
        return mRepository.getRecipeBookById(id);
    }

    public List<RecipeBook> getCurrentRecipeBooksById(int... id) {
        return mRepository.getRecipeBooksByIds(id);
    }


    // - - - Inserts & Updates - - -

    public void insert(RecipeBook... recipebooks) { mRepository.insert(recipebooks); }

    public void update(RecipeBook recipebook) { mRepository.insert(recipebook); }
}
