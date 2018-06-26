package com.hagenberg.needy.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;

@Database(entities = {Recipe.class, RecipeBook.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeData recipeData();
    public abstract RecipeBookData recipeBookData();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "needyDatabase").fallbackToDestructiveMigration().build();

                    //CouldDo: Migration statt .fallbackToDestructiveMigration()
                }
            }
        }
        return INSTANCE;
    }
}