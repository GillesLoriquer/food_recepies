package com.example.foodrecipes.repository;

import androidx.lifecycle.LiveData;

import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.network.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;

    private RecipeApiClient mRecipeApiClient;

    private RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
    }

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeApiClient.getRecipies();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        pageNumber = pageNumber == 0 ? 1 : pageNumber;  // pageNumber ne peut pas être 0
        mRecipeApiClient.searchRecipesApi(query, pageNumber);
    }

    public void cancelRequest() {
        mRecipeApiClient.cancelRequest();
    }
}
