package com.example.foodrecipes.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.foodrecipes.AppExecutors;
import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.network.ServiceGenerator;
import com.example.foodrecipes.network.response.ApiResponse;
import com.example.foodrecipes.network.response.RecipeResponse;
import com.example.foodrecipes.network.response.RecipeSearchResponse;
import com.example.foodrecipes.persistence.RecipeDao;
import com.example.foodrecipes.persistence.RecipeDatabase;
import com.example.foodrecipes.util.NetworkBoundResource;
import com.example.foodrecipes.util.Resource;

import java.util.List;

import static com.example.foodrecipes.util.Constants.RECIPE_REFRESH_TIME;

public class RecipeRepository {

    /**
     * -------------------------------- VARIABLES
     */
    private static final String TAG = "RecipeRepository";

    private static RecipeRepository instance;

    private RecipeDao mRecipeDao;

    /**
     * -------------------------------- CONSTRUCTOR
     */
    private RecipeRepository(Context context) {
        mRecipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }

    /**
     * -------------------------------- METHODS
     */
    public static RecipeRepository getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeRepository(context);
        }
        return instance;
    }

    /**
     * -------------------------------- LIVEDATA
     */
    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber) {
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse item) {
                if (item.getRecipes() != null) {
                    Recipe[] recipes = new Recipe[item.getRecipes().size()];

                    int index = 0;
                    for (long rowId : mRecipeDao.insertRecipes((Recipe[]) item.getRecipes().toArray(recipes))) {
                        if (rowId == -1) {
                            Log.d(TAG, "saveCallResult: CONFLICT. This recipe is already in cache : " + recipes[index].getTitle());

                            mRecipeDao.updateRecipe(
                                    recipes[index].getRecipeId(),
                                    recipes[index].getTitle(),
                                    recipes[index].getPublisher(),
                                    recipes[index].getImageUrl(),
                                    recipes[index].getSocialRank()
                            );
                        }
                        index++;
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return mRecipeDao.searchRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return ServiceGenerator.getRecipeApi()
                        .searchRecipes(query, String.valueOf(pageNumber));
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Recipe>> searchRecipesApi(final String recipeId) {
        return new NetworkBoundResource<Recipe, RecipeResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull RecipeResponse item) {
                if (item.getRecipe() != null) {
                    item.getRecipe().setTimestamp((int) (System.currentTimeMillis() / 1000));
                    mRecipeDao.insertRecipe(item.getRecipe());
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Recipe data) {
                Log.d(TAG, "shouldFetch: recipe: " + data.toString());
                int currentTime = (int) (System.currentTimeMillis()) / 1000;
                Log.d(TAG, "shouldFetch: current time: " + currentTime);
                int lastRefresh = data.getTimestamp();
                Log.d(TAG, "shouldFetch: last refresh: " + lastRefresh);
                int intervalTimeInDays = (currentTime - lastRefresh) / 60 / 60 / 24;
                Log.d(TAG, "shouldFetch: it's been "
                        + intervalTimeInDays
                        + " days since this recipe was refreshed");

                if (intervalTimeInDays >= RECIPE_REFRESH_TIME) {
                    Log.d(TAG, "shouldFetch: last refresh >= 30 days, should update");
                    return true;
                }
                Log.d(TAG, "shouldFetch: last refresh < 30 days, should not update");
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Recipe> loadFromDb() {
                return mRecipeDao.getRecipe(recipeId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().getRecipe(recipeId);
            }
        }.getAsLiveData();
    }
}
