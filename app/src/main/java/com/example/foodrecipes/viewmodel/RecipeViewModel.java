package com.example.foodrecipes.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.repository.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    private String mRecipeId;

    private boolean mDdidRetrieveRecipe;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mDdidRetrieveRecipe = false;
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipeRepository.getRecipe();
    }

    public LiveData<Boolean> isRecipeRequestTimeout() {
        return mRecipeRepository.isRecipeRequestTimeout();
    }

    public void searchRecipeApi(String recipeId) {
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeApi(recipeId);
    }

    public String getRecipeId() {
        return mRecipeId;
    }

    public boolean getDidRetrieveRecipe() {
        return mDdidRetrieveRecipe;
    }

    public void setDidRetrieveRecipe(boolean didRetrieveRecipe) {
        this.mDdidRetrieveRecipe = didRetrieveRecipe;
    }
}
