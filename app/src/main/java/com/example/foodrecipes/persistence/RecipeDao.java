package com.example.foodrecipes.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodrecipes.model.Recipe;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {

    @Insert(onConflict = IGNORE)
    long[] insertRecipes(Recipe... recipe);

    @Insert(onConflict = REPLACE)
    void insertRecipe(Recipe recipe);

    @Query("UPDATE recipes " +
            "SET title = :title, publisher = :publisher, image_url = :imageUrl, social_rank = :socialRank " +
            "WHERE recipe_id = :recipeId")
    LiveData<List<Recipe>> updateRecipe(String recipeId, String title, String publisher, String imageUrl, Double socialRank);

    @Query("SELECT * FROM recipes r " +
            "WHERE title LIKE '%' || :query || '%' " +
            "OR ingredients LIKE  '%' || :query || '%' " +
            "ORDER BY social_rank " +
            "DESC LIMIT (:pageNumber * 30)")
    LiveData<List<Recipe>> searchRecipes(String query, int pageNumber);

    @Query("SELECT * FROM recipes WHERE recipe_id = :recipeId")
    LiveData<Recipe> getRecipe(int recipeId);
}
