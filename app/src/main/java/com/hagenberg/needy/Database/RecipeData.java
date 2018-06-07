package com.hagenberg.needy.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.hagenberg.needy.Entity.Recipe;

import java.util.List;

@Dao
public interface RecipeData {
    @Query("SELECT * FROM Recipe")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT * FROM Recipe WHERE uid IN (:recipeIds)")
    LiveData<List<Recipe>> loadAllByIds(int[] recipeIds);

    @Query("SELECT * FROM Recipe WHERE name LIKE :name AND "
            + "description LIKE :desc LIMIT 1")
    LiveData<Recipe> findByName(String name, String desc);

    @Insert
    void insertAll(Recipe... users);

    @Delete
    void delete(Recipe user);
}
