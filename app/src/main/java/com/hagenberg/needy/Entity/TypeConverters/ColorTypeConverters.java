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

    @TypeConverter
    public static String someObjectListToString(Color someObjects) {
        if(someObjects == Color.BLUE){
            return "BLUE";
        }else if(someObjects == Color.RED){
            return "RED";
        }else if(someObjects == Color.GREEN){
            return "GREEN";
        }

        return "ORANGE";
    }
}
