package com.example.foodrecipes;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.foodrecipes.model.Recipe;

public class RecipeActivity extends BaseActivity {

    public static final String RECIPE = "RECIPE";

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

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra(RECIPE)) {
            Recipe recipe = getIntent().getParcelableExtra(RECIPE);
        }
    }
}
