package com.example.foodrecipes.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrecipes.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView recipeTitle, recipePubliser, recipeSocialScore;
    AppCompatImageView recipeImage;
    OnRecipeListener onRecipeListener;

    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
        super(itemView);

        this.onRecipeListener = onRecipeListener;

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
}
