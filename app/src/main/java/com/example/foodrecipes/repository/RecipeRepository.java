package com.example.foodrecipes.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodrecipes.model.Recipe;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;

    private MutableLiveData<List<Recipe>> mRecipies;

    public RecipeRepository() {
        mRecipies = new MutableLiveData<>();
    }

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipies;
    }
}
