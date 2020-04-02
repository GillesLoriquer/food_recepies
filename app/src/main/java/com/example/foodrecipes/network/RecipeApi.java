package com.example.foodrecipes.network;

import androidx.lifecycle.LiveData;

import com.example.foodrecipes.network.response.ApiResponse;
import com.example.foodrecipes.network.response.RecipeResponse;
import com.example.foodrecipes.network.response.RecipeSearchResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {
    // get recipe detail URL = https://recipesapi.herokuapp.com/api/get?rId=47070
    // get recipes search URL = https://recipesapi.herokuapp.com/api/search?q=chicken&page=2

    // SEARCH
    @GET("api/search")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipes(
            @Query("q") String query,
            @Query("page") String page
    );

    // GET RECIPE REQUEST
    @GET("api/get")
    LiveData<ApiResponse<RecipeResponse>> getRecipe(
            @Query("rId") String recipeId
    );
}
