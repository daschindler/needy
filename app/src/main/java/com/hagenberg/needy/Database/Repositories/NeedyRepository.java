package com.hagenberg.needy.Database.Repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.hagenberg.needy.Database.AppDatabase;
import com.hagenberg.needy.Database.RecipeData;
import com.hagenberg.needy.Entity.Recipe;

import java.util.List;

public class NeedyRepository {
    private RecipeData mRecipeData;
    private LiveData<List<Recipe>> mAllRecipes;

    public NeedyRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);

        mRecipeData = db.recipeData();
        mAllRecipes = mRecipeData.getAll();
    }

    public LiveData<List<Recipe>> getAllWords() {
        return mAllRecipes;
    }

    public void insert (Recipe word) {
        new insertAsyncTask(mRecipeData).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Recipe, Void, Void> {

        private RecipeData mRecipeAsyncTaskData;

        insertAsyncTask(RecipeData dao) {
            mRecipeAsyncTaskData = dao;
        }

        @Override
        protected Void doInBackground(final Recipe... params) {
            mRecipeAsyncTaskData.insertAll(params[0]);
            return null;
        }
    }
}
