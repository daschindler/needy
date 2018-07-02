package com.hagenberg.needy.Entity.TypeConverters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hagenberg.needy.Entity.Color;
import com.hagenberg.needy.Entity.Ingredient;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ColorTypeConverters {

    static Gson gson = new Gson();

    /**
     *
     * @param data as String
     * @return Color Object (Enum) created from the given String, returning orange as default
     */
    @TypeConverter
    public static Color stringToSomeObjectList(String data) {
        switch(data){
            case "GREEN":
                return Color.GREEN;
            case "BLUE":
                return Color.BLUE;
            case "RED":
                return Color.RED;
            default:
                return Color.ORANGE;
        }
    }

    /**
     *
     * @param someObject
     * @return the Color-Object as simple String, holding it's value
     */
    @TypeConverter
    public static String someObjectListToString(Color someObject) {
        if(someObject == Color.BLUE){
            return "BLUE";
        }else if(someObject == Color.RED){
            return "RED";
        }else if(someObject == Color.GREEN){
            return "GREEN";
        }

        return "ORANGE";
    }
}
