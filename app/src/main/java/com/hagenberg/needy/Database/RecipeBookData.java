package com.hagenberg.needy.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;

import java.util.List;

/** Direct Actions for the Database for RecipeBooks. SQL-Queries and Room-Actions are defined here */
@Dao
public interface RecipeBookData {
    @Query("SELECT * FROM RecipeBook")
    LiveData<List<RecipeBook>> getAll();

    @Query("SELECT * FROM RecipeBook WHERE uid = :recipeId LIMIT 1")
    LiveData<RecipeBook> loadById(int recipeId);

    @Query("SELECT * FROM RecipeBook WHERE uid IN (:recipeBooktIds)")
    LiveData<List<RecipeBook>> loadAllByIds(int[] recipeBooktIds);

    @Query("SELECT * FROM RecipeBook WHERE name LIKE :keyword AND "
            + "description LIKE :keyword")
    LiveData<List<RecipeBook>> findByNameOrDesc(String keyword);

    @Insert
    void insertAll(RecipeBook... recipeBooks);

    @Delete
    void delete(RecipeBook... recipeBook);

    @Update
    void update(RecipeBook... recipeBooks);
}
