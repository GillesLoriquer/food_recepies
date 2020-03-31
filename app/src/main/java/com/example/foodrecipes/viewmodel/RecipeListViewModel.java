package com.example.foodrecipes.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipe;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsViewingRecipe = false;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        mIsViewingRecipe = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }

    public boolean getIsViewingRecipe() {
        return mIsViewingRecipe;
    }

    public void setViewingRecipe(boolean isViewingRecipe) {
        this.mIsViewingRecipe = isViewingRecipe;
    }
}
