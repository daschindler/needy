package com.hagenberg.needy.Database.Repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.hagenberg.needy.Database.AppDatabase;
import com.hagenberg.needy.Database.RecipeBookData;
import com.hagenberg.needy.Database.RecipeData;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;

import java.util.List;

public class NeedyRepository {
    private RecipeData mRecipeData;
    private RecipeBookData mRecipeBookData;
    private LiveData<List<Recipe>> mAllRecipes;
    private LiveData<List<RecipeBook>> mAllRecipeBooks;

    public NeedyRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);

        mRecipeData = db.recipeData();
        mRecipeBookData = db.recipeBookData();

        mAllRecipes = mRecipeData.getAll();
        mAllRecipeBooks = mRecipeBookData.getAll();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }

    public LiveData<List<RecipeBook>> getAllRecipeBooks() {
        return mAllRecipeBooks;
    }

    public void insert (Recipe recipe) {
        new insertRecipeAsyncTask(mRecipeData).execute(recipe);
    }

    public void insert (RecipeBook recipeBook) {
        new insertRecipeBookAsyncTask(mRecipeBookData).execute(recipeBook);
    }

    public void update (Recipe recipe) {
        new updateRecipeAsyncTask(mRecipeData).execute(recipe);
    }

    public void update (RecipeBook recipeBook) {
        new updateRecipeBookAsyncTask(mRecipeBookData).execute(recipeBook);
    }

    private static class insertRecipeAsyncTask extends AsyncTask<Recipe, Void, Void> {

        private RecipeData mRecipeAsyncTaskData;

        insertRecipeAsyncTask(RecipeData dao) {
            mRecipeAsyncTaskData = dao;
        }

        @Override
        protected Void doInBackground(final Recipe... params) {
            mRecipeAsyncTaskData.insertAll(params[0]);
            return null;
        }
    }

    private static class insertRecipeBookAsyncTask extends AsyncTask<RecipeBook, Void, Void> {

        private RecipeBookData mRecipeBookAsyncTaskData;

        insertRecipeBookAsyncTask(RecipeBookData dao) {
            mRecipeBookAsyncTaskData = dao;
        }

        @Override
        protected Void doInBackground(final RecipeBook... params) {
            mRecipeBookAsyncTaskData.insertAll(params[0]);
            return null;
        }
    }

    private static class updateRecipeAsyncTask extends AsyncTask<Recipe, Void, Void> {

        private RecipeData mRecipeAsyncTaskData;

        updateRecipeAsyncTask(RecipeData dao) {
            mRecipeAsyncTaskData = dao;
        }

        @Override
        protected Void doInBackground(final Recipe... params) {
            mRecipeAsyncTaskData.update(params[0]);
            return null;
        }
    }

    private static class updateRecipeBookAsyncTask extends AsyncTask<RecipeBook, Void, Void> {

        private RecipeBookData mRecipeBookAsyncTaskData;

        updateRecipeBookAsyncTask(RecipeBookData dao) {
            mRecipeBookAsyncTaskData = dao;
        }

        @Override
        protected Void doInBackground(final RecipeBook... params) {
            mRecipeBookAsyncTaskData.update(params[0]);
            return null;
        }
    }
}
