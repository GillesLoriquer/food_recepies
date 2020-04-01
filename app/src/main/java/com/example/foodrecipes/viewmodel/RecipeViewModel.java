package com.example.foodrecipes.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.repository.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    private String mRecipeId;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeApi(String recipeId) {
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeApi(recipeId);
    }

    public String getRecipeId() {
        return mRecipeId;
    }
}
