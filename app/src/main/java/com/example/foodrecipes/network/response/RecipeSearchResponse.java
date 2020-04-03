package com.example.foodrecipes.network.response;

import com.example.foodrecipes.model.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeSearchResponse {

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("recipes")
    @Expose
    private List<Recipe> recipes;

    @Expose
    private String error;

    public int getCount() {
        return count;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "RecipeSearchResponse{" +
                "count=" + count +
                ", recipes=" + recipes +
                ", error='" + error + '\'' +
                '}';
    }
}
