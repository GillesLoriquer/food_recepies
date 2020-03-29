package com.example.foodrecipes.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }
}
