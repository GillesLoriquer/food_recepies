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
import com.example.foodrecipes.network.response.RecipeSearchResponse;
import com.example.foodrecipes.persistence.RecipeDao;
import com.example.foodrecipes.persistence.RecipeDatabase;
import com.example.foodrecipes.util.NetworkBoundResource;
import com.example.foodrecipes.util.Resource;

import java.util.List;

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
                            Log.d(TAG, "saveCallResult: CONFLICT. This recipe is already in cache");

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
}
