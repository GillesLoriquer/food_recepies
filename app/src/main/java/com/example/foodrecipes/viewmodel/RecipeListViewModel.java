package com.example.foodrecipes.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        mIsViewingRecipes = true;
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }

    public boolean getIsViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipe) {
        this.mIsViewingRecipes = isViewingRecipe;
    }

    public boolean onBackPressed() {
        if (mIsPerformingQuery) {
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
        if (mIsViewingRecipes) {
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }

    public boolean isPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void setPerformingQuery(boolean performingQuery) {
        mIsPerformingQuery = performingQuery;
    }
}
