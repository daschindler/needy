package com.hagenberg.needy.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.hagenberg.needy.Entity.Recipe;

import java.util.List;

@Dao
public interface RecipeBookData {
    @Query("SELECT * FROM RecipeBook")
    List<Recipe> getAll();

    @Query("SELECT * FROM RecipeBook WHERE uid IN (:recipeBooktIds)")
    List<Recipe> loadAllByIds(int[] recipeBooktIds);

    @Query("SELECT * FROM RecipeBook WHERE name LIKE :name AND "
            + "description LIKE :desc LIMIT 1")
    Recipe findByName(String name, String desc);

    @Insert
    void insertAll(Recipe... users);

    @Delete
    void delete(Recipe user);
}
