package com.example.foodrecipes.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.network.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;

    private RecipeApiClient mRecipeApiClient;

    private String mQuery;

    private int mPageNumber;

    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();

    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

    private RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
        initMediators();
    }

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    private void initMediators() {
        LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();
        mRecipes.addSource(recipeListApiSource, recipes -> {
            if (recipes != null) {
                mRecipes.setValue(recipes);
                doneQuery(recipes);
            } else {
                // TODO : search data from db cache
                doneQuery(null);
            }
        });
    }

    public LiveData<Boolean> isQueryExhausted() {
        return mIsQueryExhausted;
    }

    public void doneQuery(List<Recipe> list) {
        if (list != null) {
            if (list.size() <= 30) {
                mIsQueryExhausted.setValue(true);
            }
        } else {
            mIsQueryExhausted.setValue(false);
        }
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipeApiClient.getRecipe();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        pageNumber = pageNumber == 0 ? 1 : pageNumber;  // pageNumber ne peut pas être 0
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
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
