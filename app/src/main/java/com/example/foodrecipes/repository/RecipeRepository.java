package com.example.foodrecipes.repository;

import androidx.lifecycle.LiveData;

import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.network.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;

    private RecipeApiClient mRecipeApiClient;

    private String mQuery;

    private int mPageNumber;

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
        return mRecipeApiClient.getRecipes();
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipeApiClient.getRecipe();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        pageNumber = pageNumber == 0 ? 1 : pageNumber;  // pageNumber ne peut pas être 0
        mQuery = query;
        mPageNumber = pageNumber;
        mRecipeApiClient.searchRecipesApi(query, pageNumber);
    }

    public void searchRecipeApi(String recipeId) {
        mRecipeApiClient.searchRecipeApi(recipeId);
    }

    public void searchNextPage() {
        searchRecipesApi(mQuery, mPageNumber + 1);
    }

    public void cancelRequest() {
        mRecipeApiClient.cancelRequest();
    }

    public LiveData<Boolean> isRecipeRequestTimeout() {
        return mRecipeApiClient.isRecipeRequestTimeout();
    }
}
