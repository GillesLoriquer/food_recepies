package com.example.foodrecipes.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.foodrecipes.R;
import com.example.foodrecipes.model.Recipe;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnRecipeListener onRecipeListener;
    private CircleImageView categoryImage;
    private TextView categoryTitle;
    private RequestManager requestManager;

    CategoryViewHolder(
            @NonNull View itemView,
            OnRecipeListener onRecipeListener,
            RequestManager requestManager) {
        super(itemView);
        this.onRecipeListener = onRecipeListener;
        this.requestManager = requestManager;

        this.categoryImage = itemView.findViewById(R.id.category_image);
        this.categoryTitle = itemView.findViewById(R.id.category_title);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onRecipeListener.onCategoryClick(categoryTitle.getText().toString());
    }

    void onBind(Recipe recipe) {
        Uri path = Uri.parse("android.resource://com.example.foodrecipes/drawable/"
                + recipe.getImageUrl());

        requestManager
                .load(path)
                .into(this.categoryImage);

        this.categoryTitle.setText(recipe.getTitle());
    }
}
