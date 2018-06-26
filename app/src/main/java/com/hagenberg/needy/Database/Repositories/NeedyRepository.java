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



    // - - - Recipe Methods

    public LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }

    public LiveData<List<Recipe>> getRecipesByNameOrDesc(String keyword) { return mRecipeData.findByNameOrDesc(keyword); }

    public LiveData<Recipe> getRecipeById(int id) {
        LiveData<Recipe> recipe = mRecipeData.loadById(id);
        return recipe;
    }

    public List<Recipe> getRecipesByIds(int[] ids) {
        if (ids.length == 0)
            return null;

        LiveData<List<Recipe>> recipes =  mRecipeData.loadAllByIds(ids);
        if(recipes == null)
            return null;

        return recipes.getValue();
    }

    public void insert (Recipe... recipe) {
        new insertRecipeAsyncTask(mRecipeData).execute(recipe);
    }

    public void update (Recipe... recipe) {
        new updateRecipeAsyncTask(mRecipeData).execute(recipe);
    }

    public void delete (Recipe... recipe){
        new deleteRecipeAsyncTask(mRecipeData).execute(recipe);
    }



    /// - - - RecipeBook Methods

    public LiveData<RecipeBook> getRecipeBookById(int id) {
        return mRecipeBookData.loadById(id);
    }

    public LiveData<List<RecipeBook>> getAllRecipeBooks() {
        return mAllRecipeBooks;
    }

    public LiveData<List<RecipeBook>> getRecipeBooksByNameOrDesc(String keyword) { return mRecipeBookData.findByNameOrDesc(keyword); }

    public LiveData<List<RecipeBook>> getRecipeBooksByIds(int[] ids) {
        return mRecipeBookData.loadAllByIds(ids);
    }

    public void insert (RecipeBook... recipeBooks) {
        new insertRecipeBookAsyncTask(mRecipeBookData).execute(recipeBooks);
    }

    public void update (RecipeBook... recipeBook) {
        new updateRecipeBookAsyncTask(mRecipeBookData).execute(recipeBook);
    }

    public void delete (RecipeBook... recipeBook){
        new deleteRecipeBookAsyncTask(mRecipeBookData).execute(recipeBook);
    }



    /// Recipe Tasks

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

    private static class deleteRecipeAsyncTask extends AsyncTask<Recipe, Void, Void> {

        private RecipeData mRecipeAsyncTaskData;

        deleteRecipeAsyncTask(RecipeData dao) {
            mRecipeAsyncTaskData = dao;
        }

        @Override
        protected Void doInBackground(final Recipe... params) {
            mRecipeAsyncTaskData.delete(params);
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




    // RecipeBook Tasks

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

    private static class deleteRecipeBookAsyncTask extends AsyncTask<RecipeBook, Void, Void> {

        private RecipeBookData mRecipeBookAsyncTaskData;

        deleteRecipeBookAsyncTask(RecipeBookData dao) {
            mRecipeBookAsyncTaskData = dao;
        }

        @Override
        protected Void doInBackground(final RecipeBook... params) {
            mRecipeBookAsyncTaskData.delete(params);
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
            mRecipeBookAsyncTaskData.update(params);
            return null;
        }
    }
}
