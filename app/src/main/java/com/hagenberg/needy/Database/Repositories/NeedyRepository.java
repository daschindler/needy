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

/** The main repository for database-access. Holding the database and Data-Instances */
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

    /**
     *
     * @return all stored recipes
     */
    public LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }

    /**
     *
     * @param keyword
     * @return all stored recipes filtered by a keyword
     */
    public LiveData<List<Recipe>> getRecipesByNameOrDesc(String keyword) { return mRecipeData.findByNameOrDesc(keyword); }

    /**
    /**
     *
     * @param id
     * @return Returning a recipe by its id
     */
    public LiveData<Recipe> getRecipeById(int id) {
        LiveData<Recipe> recipe = mRecipeData.loadById(id);
        return recipe;
    }

    /**
     *
     * @param ids
     * @return Returning recipies by a list of id
     */
    public List<Recipe> getRecipesByIds(int[] ids) {
        if (ids.length == 0)
            return null;

        LiveData<List<Recipe>> recipes =  mRecipeData.loadAllByIds(ids);
        if(recipes == null)
            return null;

        return recipes.getValue();
    }

    /**
     * insert a recipe
     * @param recipe
     */
    public void insert (Recipe... recipe) {
        new insertRecipeAsyncTask(mRecipeData).execute(recipe);
    }

    /**
     * update a recipe
     * @param recipe
     */
    public void update (Recipe... recipe) {
        new updateRecipeAsyncTask(mRecipeData).execute(recipe);
    }

    /**
     * delete a recipe
     * @param recipe
     */
    public void delete (Recipe... recipe){
        new deleteRecipeAsyncTask(mRecipeData).execute(recipe);
    }



    /// - - - RecipeBook Methods
    /**
     *
     * @param id
     * @return Returning a recipebook by its id
     */
    public LiveData<RecipeBook> getRecipeBookById(int id) {
        return mRecipeBookData.loadById(id);
    }

    /**
     *
     * @return all stored recipebooks
     */
    public LiveData<List<RecipeBook>> getAllRecipeBooks() {
        return mAllRecipeBooks;
    }

    /**
     *
     * @param keyword
     * @return Returning all stored recipebooks filtered by a keyword
     */
    public LiveData<List<RecipeBook>> getRecipeBooksByNameOrDesc(String keyword) { return mRecipeBookData.findByNameOrDesc(keyword); }

    /**
     *
     * @param ids
     * @return Returning all stored recipebooks by a list of ids
     */
    public LiveData<List<RecipeBook>> getRecipeBooksByIds(int[] ids) {
        return mRecipeBookData.loadAllByIds(ids);
    }

    /**
     * insert a recipebook
     * @param recipeBooks
     */
    public void insert (RecipeBook... recipeBooks) {
        new insertRecipeBookAsyncTask(mRecipeBookData).execute(recipeBooks);
    }

    /**
     * update a recipebook
     * @param recipeBook
     */
    public void update (RecipeBook... recipeBook) {
        new updateRecipeBookAsyncTask(mRecipeBookData).execute(recipeBook);
    }


    /**
     * delete a recipebook
     * @param recipeBook
     */
    public void delete (RecipeBook... recipeBook){
        new deleteRecipeBookAsyncTask(mRecipeBookData).execute(recipeBook);
    }



    /// Recipe Tasks
    /** AsyncTask for inserting a recipe. So UI Thread can do it's thing meanwhile */
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

    /** AsyncTask for deleting a recipe. So UI Thread can do it's thing meanwhile */
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

    /** AsyncTask for updating a recipe. So UI Thread can do it's thing meanwhile */
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

    /** AsyncTask for inserting a recipebook. So UI Thread can do it's thing meanwhile */
    private static class insertRecipeBookAsyncTask extends AsyncTask<RecipeBook, Void, Void> {

        private RecipeBookData mRecipeBookAsyncTaskData;

        insertRecipeBookAsyncTask(RecipeBookData dao) {
            mRecipeBookAsyncTaskData = dao;
        }

        @Override
        protected Void doInBackground(final RecipeBook... params) {
            mRecipeBookAsyncTaskData.insertAll(params);
            return null;
        }
    }

    /** AsyncTask for deleting a recipebook. So UI Thread can do it's thing meanwhile */
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

    /** AsyncTask for updating a recipebook. So UI Thread can do it's thing meanwhile */
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
