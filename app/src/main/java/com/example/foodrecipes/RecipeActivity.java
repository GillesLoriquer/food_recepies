package com.example.foodrecipes;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.viewmodel.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

    public static final String RECIPE = "RECIPE";

    private RecipeViewModel mRecipeViewModel;

    // UI components
    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;
    private ScrollView mScrollView;         // Needed to set visibility to VISIBLE at some point

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent_scrollview);

        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        subscribeObservers();

        getIncomingIntent();
    }

    private void subscribeObservers() {
        mRecipeViewModel.getRecipe().observe(this, recipe -> {
            if (recipe != null) {
                Log.d(TAG, "subscribeObservers: ----------------------------------------");
                Log.d(TAG, "subscribeObservers: " + recipe.getTitle());
                for (String ingredient : recipe.getIngredients()) {
                    Log.d(TAG, "subscribeObservers: " + ingredient);
                }
            }
        });
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra(RECIPE)) {
            Recipe recipe = getIntent().getParcelableExtra(RECIPE);
            mRecipeViewModel.searchRecipeApi(recipe.getRecipeId());
        }
    }
}
