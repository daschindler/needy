package com.hagenberg.needy.Entity.TypeConverters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hagenberg.needy.Entity.Ingredient;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class IngredientsTypeConverters {

    static Gson gson = new Gson();

    /**
     *
     * @param data as JsonString
     * @return a List of Ingredients as Objects from the given Json-String
     */
    @TypeConverter
    public static List<Ingredient> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Ingredient>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    /**
     *
     * @param someObjects as List of Ingredients
     * @return the given List of Ingredients as Json-String
     */
    @TypeConverter
    public static String someObjectListToString(List<Ingredient> someObjects) {
        String gs =  gson.toJson(someObjects);

        return gs;
    }
}
