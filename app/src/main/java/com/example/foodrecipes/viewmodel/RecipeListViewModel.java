package com.example.foodrecipes.viewmodel;

import android.app.Application;
import android.util.Log;

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

    public static final String QUERY_EXHAUSTED = "No more result.";

    public enum ViewState {CATEGORIES, RECIPES}

    private MutableLiveData<ViewState> mViewState;

    private RecipeRepository mRecipeRepository;

    private MediatorLiveData<Resource<List<Recipe>>> mRecipes = new MediatorLiveData<>();

    private boolean mIsPerformingQuery;

    private boolean mIsQueryExhausted;

    private String mQuery;

    private int mPageNumber;

    private boolean mCancelRequest;

    private long mRequestStartTime;

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

    public int getPageNumber() {
        return mPageNumber;
    }

    /**
     * -------------------------------- SETTERS
     */
    public void setViewState(ViewState viewState) {
        mViewState.setValue(viewState);
    }

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
        if (!mIsPerformingQuery) {
            this.mPageNumber = pageNumber == 0 ? 1 : pageNumber;     // page 0 et 1 affiche le même résultat
            this.mQuery = query;
            this.mIsQueryExhausted = false;
            executeSearch();
        }
    }

    private void executeSearch() {
        mRequestStartTime = System.currentTimeMillis();
        this.mCancelRequest = false;
        this.mIsPerformingQuery = true;
        this.mViewState.setValue(ViewState.RECIPES);

        final LiveData<Resource<List<Recipe>>> repositoryDataSource =
                mRecipeRepository.searchRecipesApi(mQuery, mPageNumber);

        mRecipes.addSource(repositoryDataSource, listResource -> {
            if (!mCancelRequest) {
                if (listResource != null) {
                    this.mRecipes.setValue(listResource);
                    if (listResource.status == Resource.Status.SUCCESS) {
                        Log.d(TAG, "executeSearch: REQUEST TIME: " + (System.currentTimeMillis() - mRequestStartTime) / 1000 + " seconds.");
                        this.mIsPerformingQuery = false;
                        if (listResource.data != null) {
                            if (listResource.data.size() == 0) {
                                Log.d(TAG, "executeSearch: query is exhausted");
                                this.mRecipes.setValue(
                                        new Resource<>(
                                                Resource.Status.ERROR,
                                                listResource.data,
                                                QUERY_EXHAUSTED
                                        )
                                );
                            }
                        }
                        this.mRecipes.removeSource(repositoryDataSource);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        Log.d(TAG, "executeSearch: REQUEST TIME: " + (System.currentTimeMillis() - mRequestStartTime) / 1000 + " seconds.");
                        this.mIsPerformingQuery = false;
                        this.mRecipes.removeSource(repositoryDataSource);
                    }
                } else {
                    this.mRecipes.removeSource(repositoryDataSource);
                }
            } else {
                this.mRecipes.removeSource(repositoryDataSource);
            }
        });
    }

    public void cancelSearchRequest() {
        if (mIsPerformingQuery) {
            Log.d(TAG, "cancelSearchRequest: cancelling the search request.");
            this.mCancelRequest = true;
            this.mIsPerformingQuery = false;
            this.mPageNumber = 1;
        }
    }

    public void searchNextPage() {
        if (!mIsPerformingQuery && !mIsQueryExhausted) {
            this.mPageNumber++;
            executeSearch();
        }
    }

    public void setViewCategories() {
        this.mViewState.setValue(ViewState.CATEGORIES);
    }
}
