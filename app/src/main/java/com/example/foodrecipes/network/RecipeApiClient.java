package com.example.foodrecipes.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodrecipes.AppExecutors;
import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.network.response.RecipeSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.foodrecipes.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;

    private MutableLiveData<List<Recipe>> mRecipies;

    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;

    private RecipeApiClient() {
        mRecipies = new MutableLiveData<>();
    }

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipies() {
        return mRecipies;
    }

    public void searchRecipesApi(String query, int pageNumber) {
        if (mRetrieveRecipesRunnable != null) mRetrieveRecipesRunnable = null;
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query, pageNumber);

        final Future handler = AppExecutors.getInstance().getNetworkIO().submit(mRetrieveRecipesRunnable);

        AppExecutors.getInstance().getNetworkIO().schedule(() -> {
            // let the user know its timed out
            handler.cancel(true);
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    // classe définissant le Runnable spécifique à une recherche de type 'search' (RecipeApi)
    private class RetrieveRecipesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber) {
            return ServiceGenerator.getRecipeApi().searchRecipe(query, String.valueOf(pageNumber));
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: cancelling the search request");
            cancelRequest = true;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNumber).execute();

                if (cancelRequest) return;

                if (response.code() == 200) {
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse) response.body()).getRecipes());
                    if (pageNumber == 1) {
                        mRecipies.postValue(list);  // setValue = main thread, postValue = background thread
                    } else {
                        List<Recipe> currentRecipes = mRecipies.getValue();
                        currentRecipes.addAll(list);
                        mRecipies.postValue(currentRecipes);
                    }
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mRecipies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipies.postValue(null);
            }
        }
    }
}
