package com.example.foodrecipes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.foodrecipes.R;
import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * -------------------------------- VARIABLES
     */
    private static final int RECIPE_TYPE = 1;

    private static final int LOADING_TYPE = 2;

    private static final int CATEGORY_TYPE = 3;

    private static final int EXHAUSTED_TYPE = 4;

    private static final String LOADING = "LOADING";

    private static final String EXHAUSTED = "EXHAUSTED";

    private List<Recipe> mRecipeList;

    private OnRecipeListener mOnRecipeListener;

    private RequestManager mRequestManager;

    /**
     * -------------------------------- CONSTRUCTOR
     */
    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener, RequestManager requestManager) {
        mOnRecipeListener = onRecipeListener;
        this.mRequestManager = requestManager;
    }

    /**
     * -------------------------------- BUILT-IN METHODS
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case LOADING_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            }
            case CATEGORY_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_category_list_item, parent, false);
                return new CategoryViewHolder(view, mOnRecipeListener, mRequestManager);
            }
            case EXHAUSTED_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_search_exhausted, parent, false);
                return new SearchExhaustedViewHolder(view);
            }
            default: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_recipe_list_item, parent, false);
                return new RecipeViewHolder(view, mOnRecipeListener, mRequestManager);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);

        if (itemViewType == RECIPE_TYPE) {
            ((RecipeViewHolder) holder).onBind(mRecipeList.get(position));

        } else if (itemViewType == CATEGORY_TYPE) {
            ((CategoryViewHolder) holder).onBind(mRecipeList.get(position));
        }
    }

    // getItemViewType est appelée avant onCreateViewHolder afin de lui fournir le viewType
    @Override
    public int getItemViewType(int position) {
        if (mRecipeList.get(position).getSocialRank() == -1D) {
            return CATEGORY_TYPE;
        } else if (mRecipeList.get(position).getTitle().equals(LOADING)) {
            return LOADING_TYPE;
        } else if (mRecipeList.get(position).getTitle().equals(EXHAUSTED)) {
            return EXHAUSTED_TYPE;
        }
        return RECIPE_TYPE;
    }

    @Override
    public int getItemCount() {
        return mRecipeList != null ? mRecipeList.size() : 0;
    }

    /**
     * -------------------------------- METHODS
     */
    // display loading during request
    public void displayOnlyLoading() {
        clearRecipesList();
        Recipe recipe = new Recipe();
        recipe.setTitle(LOADING);
        this.mRecipeList.add(recipe);
        notifyDataSetChanged();
    }

    // pagination loading
    public void displayLoading() {
        if (this.mRecipeList == null) {
            this.mRecipeList = new ArrayList<>();
        }
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setTitle(LOADING);
            this.mRecipeList.add(recipe);
            notifyDataSetChanged();
        }
    }

    public void hideLoading() {
        if (isLoading()) {
            if (this.mRecipeList.get(0).getTitle().equals(LOADING)) {
                this.mRecipeList.remove(0);
            } else if (this.mRecipeList.get(this.mRecipeList.size() - 1).getTitle().equals(LOADING)) {
                this.mRecipeList.remove(this.mRecipeList.size() - 1);
            }
            notifyDataSetChanged();
        }
    }

    public void setQueryExhausted() {
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setTitle(EXHAUSTED);
        mRecipeList.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    public void displayCategories() {
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            String title = Constants.DEFAULT_SEARCH_CATEGORIES[i];
            Recipe recipe = new Recipe();
            recipe.setTitle(title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase());
            recipe.setImageUrl(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setSocialRank(-1D);
            categories.add(recipe);
        }
        mRecipeList = categories;
        notifyDataSetChanged();
    }


    /**
     * -------------------------------- HELPER METHODS
     */
    private boolean isLoading() {
        if (mRecipeList != null && mRecipeList.size() > 0) {
            return mRecipeList.get(mRecipeList.size() - 1).getTitle().equals(LOADING);
        }
        return false;
    }

    private void clearRecipesList() {
        if (this.mRecipeList != null) {
            this.mRecipeList.clear();
        } else {
            this.mRecipeList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void setRecipeList(List<Recipe> recipeList) {
        mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    public Recipe getSelectedRecipe(int position) {
        if (mRecipeList != null && mRecipeList.size() > 0) {
            return mRecipeList.get(position);
        }
        return null;
    }
}

