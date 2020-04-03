package com.example.foodrecipes.network;

import com.example.foodrecipes.util.Constants;
import com.example.foodrecipes.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.foodrecipes.util.Constants.CONNECTION_TIMEOUT;
import static com.example.foodrecipes.util.Constants.READ_TIMEOUT;
import static com.example.foodrecipes.util.Constants.WRITE_TIMEOUT;

public class ServiceGenerator {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)   // establish connection to server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)            // time between each byte read from server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)          // time between each byte sent to server
            .retryOnConnectionFailure(false)
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    public static RecipeApi getRecipeApi() {
        return recipeApi;
    }
}
