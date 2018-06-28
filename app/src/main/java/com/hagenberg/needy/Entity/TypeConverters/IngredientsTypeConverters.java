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

    @TypeConverter
    public static List<Ingredient> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Ingredient>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Ingredient> someObjects) {
        String gs =  gson.toJson(someObjects);

        return gs;
    }
}
