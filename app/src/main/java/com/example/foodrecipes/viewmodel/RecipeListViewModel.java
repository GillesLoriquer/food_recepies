package com.example.foodrecipes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

// AndroidViewModel permet d'accéder au context de l'application, contrairement au ViewModel
public class RecipeListViewModel extends AndroidViewModel {

    /**
     * -------------------------------- VARIABLES
     */
    private static final String TAG = "RecipeListViewModel";

    public enum ViewState {CATEGORIES, RECIPES}

    private MutableLiveData<ViewState> mViewState;

    /**
     * -------------------------------- CONSTRUCTOR
     */
    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    /**
     * -------------------------------- GETTERS
     */
    public MutableLiveData<ViewState> getViewState() {
        return mViewState;
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
}
