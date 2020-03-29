package com.example.foodrecipes.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodrecipes.model.Recipe;

import java.util.List;

public class RecipeApiClient {

    private static RecipeApiClient instance;

    private MutableLiveData<List<Recipe>> mRecipies;

    private RecipeApiClient() {
        mRecipies = new MutableLiveData<>();
    }

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipies() {
        return mRecipies;
    }
}
