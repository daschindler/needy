package com.hagenberg.needy.Entity.TypeConverters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hagenberg.needy.Entity.Recipe;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class RecipeTypeConverters {

    static Gson gson = new Gson();

    /**
     *
     * @param data as Json-String
     * @return a List of Recipes by the given json-string
     */
    @TypeConverter
    public static List<Recipe> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Recipe>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    /**
     *
     * @param someObjects as List of Recipes
     * @return a Json-String from the given Recipe-List
     */
    @TypeConverter
    public static String someObjectListToString(List<Recipe> someObjects) {
        return gson.toJson(someObjects);
    }
}
