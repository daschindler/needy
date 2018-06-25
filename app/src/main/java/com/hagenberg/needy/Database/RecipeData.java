package com.hagenberg.needy.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hagenberg.needy.Entity.Recipe;

import java.util.List;

@Dao
public interface RecipeData {
    @Query("SELECT * FROM Recipe")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT * FROM Recipe")
    List<Recipe> getAllCurrent();

    @Query("SELECT * FROM Recipe WHERE uid = :recipeId LIMIT 1")
    Recipe loadById(int recipeId);

    @Query("SELECT * FROM Recipe WHERE uid IN (:recipeIds)")
    LiveData<List<Recipe>> loadAllByIds(int[] recipeIds);

    @Query("SELECT * FROM Recipe WHERE name LIKE :keyword AND "
            + "description LIKE :keyword")
    LiveData<List<Recipe>> findByNameOrDesc(String keyword);

    @Insert
    void insertAll(Recipe... recipes);

    @Delete
    void delete(Recipe recipe);

    @Update
    void update(Recipe recipe);
}
