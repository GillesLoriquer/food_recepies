package com.example.foodrecipes.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodrecipes.R;
import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final String LOADING = "LOADING";
    private static final String EXHAUSTED = "EXHAUSTED";

    private List<Recipe> mRecipeList;
    private OnRecipeListener mOnRecipeListener;

    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener) {
        mOnRecipeListener = onRecipeListener;
    }

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
                return new CategoryViewHolder(view, mOnRecipeListener);
            }
            default: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_recipe_list_item, parent, false);
                return new RecipeViewHolder(view, mOnRecipeListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);

        if (itemViewType == RECIPE_TYPE) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);
            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(mRecipeList.get(position).getImageUrl())
                    .into(((RecipeViewHolder) holder).recipeImage);

            ((RecipeViewHolder) holder).recipeTitle.setText(mRecipeList.get(position).getTitle());
            ((RecipeViewHolder) holder).recipePubliser.setText(mRecipeList.get(position).getPublisher());
            ((RecipeViewHolder) holder).recipeSocialScore
                    .setText(String.valueOf(Math.round(mRecipeList.get(position).getSocialRank())));
        } else if (itemViewType == CATEGORY_TYPE) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Uri path = Uri.parse("android.resource://com.example.foodrecipes/drawable/"
                    + mRecipeList.get(position).getImageUrl());

            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(path)
                    .into(((CategoryViewHolder) holder).categoryImage);

            ((CategoryViewHolder) holder).categoryTitle.setText(mRecipeList.get(position).getTitle());
        }
    }

    // getItemViewType est appelée avant onCreateViewHolder afin de lui fournir le viewType
    @Override
    public int getItemViewType(int position) {
        if (mRecipeList.get(position).getTitle().equals(LOADING)) {
            return LOADING_TYPE;
        } else if (position == mRecipeList.size() - 1
                && position != 0
                && !mRecipeList.get(position).getTitle().equals(EXHAUSTED)) {
            return LOADING_TYPE;
        } else if (mRecipeList.get(position).getSocialRank() == -1) {
            return CATEGORY_TYPE;
        }
        return RECIPE_TYPE;
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

    public void displayLoading() {
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setTitle(LOADING);
            List<Recipe> recipes = new ArrayList<>();
            recipes.add(recipe);
            mRecipeList = recipes;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (mRecipeList != null && mRecipeList.size() > 0) {
            return mRecipeList.get(mRecipeList.size() - 1).getTitle().equals(LOADING);
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mRecipeList != null ? mRecipeList.size() : 0;
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

