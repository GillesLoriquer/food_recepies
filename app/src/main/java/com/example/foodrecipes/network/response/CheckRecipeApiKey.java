package com.example.foodrecipes.network.response;

public class CheckRecipeApiKey {

    protected static boolean isRecipeApiKeyValid(RecipeSearchResponse response) {
        return response.getError() == null;
    }

    protected static boolean isRecipeApiKeyValid(RecipeResponse response) {
        return response.getError() == null;
    }
}
