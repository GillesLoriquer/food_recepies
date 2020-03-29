package com.example.foodrecipes;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.foodrecipes.util.Testing;
import com.example.foodrecipes.viewmodel.RecipeListViewModel;

public class RecipeListActivity extends BaseActivity {

    private static final String TAG = "RecipeListActivity";

    private RecipeListViewModel mRecipeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        subscribeObservers();

        findViewById(R.id.button).setOnClickListener(v -> testRetrofitRequest("chicken", 1));
    }

    private void subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, recipes -> {
            // update UI
            Testing.printRecepies(recipes, "RecipesTest");
        });
    }

    private void testRetrofitRequest(String query, int pageNumber) {
        searchRecipesApi(query, pageNumber);
    }

    private void searchRecipesApi(String query, int pageNumber) {
        mRecipeListViewModel.searchRecipesApi(query, pageNumber);
    }
}
