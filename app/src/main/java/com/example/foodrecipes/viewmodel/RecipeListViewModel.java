package com.example.foodrecipes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.repository.RecipeRepository;
import com.example.foodrecipes.util.Resource;

import java.util.List;

// AndroidViewModel permet d'accéder au context de l'application, contrairement au ViewModel
public class RecipeListViewModel extends AndroidViewModel {

    /**
     * -------------------------------- VARIABLES
     */
    private static final String TAG = "RecipeListViewModel";

    public enum ViewState {CATEGORIES, RECIPES}

    private MutableLiveData<ViewState> mViewState;

    private RecipeRepository mRecipeRepository;

    private MediatorLiveData<Resource<List<Recipe>>> mRecipes = new MediatorLiveData<>();

    /**
     * -------------------------------- CONSTRUCTOR
     */
    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        mRecipeRepository = RecipeRepository.getInstance(application);
        init();
    }

    /**
     * -------------------------------- GETTERS
     */
    public MutableLiveData<ViewState> getViewState() {
        return mViewState;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return mRecipes;
    }

    /**
     * -------------------------------- SETTERS
     */

    /**
     * -------------------------------- METHODS
     */
    private void init() {
        if (mViewState == null) {
            mViewState = new MutableLiveData<>();
            mViewState.setValue(ViewState.CATEGORIES);
        }
    }

    public void searchRecipesApi(String query, int pageNumber) {
        final LiveData<Resource<List<Recipe>>> repositoryDataSource =
                mRecipeRepository.searchRecipesApi(query, pageNumber);

        mRecipes.addSource(repositoryDataSource, listResource -> mRecipes.setValue(listResource));
    }
}
