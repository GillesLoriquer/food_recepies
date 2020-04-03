package com.example.foodrecipes.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.foodrecipes.R;
import com.example.foodrecipes.model.Recipe;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView recipeTitle, recipePubliser, recipeSocialScore;
    private AppCompatImageView recipeImage;
    private OnRecipeListener onRecipeListener;
    private RequestManager requestManager;

    RecipeViewHolder(
            @NonNull View itemView,
            OnRecipeListener onRecipeListener,
            RequestManager requestManager) {
        super(itemView);
        this.onRecipeListener = onRecipeListener;
        this.requestManager = requestManager;

        recipeTitle = itemView.findViewById(R.id.recipe_title);
        recipePubliser = itemView.findViewById(R.id.recipe_publisher);
        recipeSocialScore = itemView.findViewById(R.id.recipe_social_score);
        recipeImage = itemView.findViewById(R.id.recipe_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }

    void onBind(Recipe recipe) {
        requestManager
                .load(recipe.getImageUrl())
                .into(this.recipeImage);

        this.recipeTitle.setText(recipe.getTitle());
        this.recipePubliser.setText(recipe.getPublisher());
        this.recipeSocialScore.setText(String.valueOf(Math.round(recipe.getSocialRank())));
    }
}
