package com.hagenberg.needy.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.hagenberg.needy.Entity.TypeConverters.RecipeTypeConverters;

import java.util.List;

@Entity
public class RecipeBook {

    public RecipeBook(){ }

    // --- Fields ---

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "recipies")
    @TypeConverters(RecipeTypeConverters.class)
    private List<Recipe> recipies;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Recipe> getRecipies() {
        return recipies;
    }

    public void setRecipies(List<Recipe> recipies) {
        this.recipies = recipies;
    }
}
