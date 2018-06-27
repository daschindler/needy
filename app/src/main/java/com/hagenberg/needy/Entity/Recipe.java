package com.hagenberg.needy.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.hagenberg.needy.Entity.TypeConverters.ColorTypeConverters;
import com.hagenberg.needy.Entity.TypeConverters.IngredientsTypeConverters;

import java.util.List;

@Entity
public class Recipe {
    public Recipe(String name, String description, List<Ingredient> ingredients){
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
    }

    // --- Fields ---

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "color")
    @TypeConverters(ColorTypeConverters.class)
    private Color color;

    @ColumnInfo(name = "ingredients")
    @TypeConverters(IngredientsTypeConverters.class)
    private List<Ingredient> ingredients;

    @ColumnInfo(name = "description")
    private String description;

    // --- Getter & Setter ---

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

