
package com.example.foodrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe implements Parcelable {

    private String title;
    private String publisher;
    private List<String> ingredients;
    @SerializedName("recipe_id")
    private String recipeId;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("social_rank")
    private Double socialRank;

    // Constructors
    public Recipe() {
    }

    public Recipe(String title, String publisher, List<String> ingredients, String recipeId, String imageUrl, Double socialRank) {
        this.title = title;
        this.publisher = publisher;
        this.ingredients = ingredients;
        this.recipeId = recipeId;
        this.imageUrl = imageUrl;
        this.socialRank = socialRank;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Double getSocialRank() {
        return socialRank;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSocialRank(Double socialRank) {
        this.socialRank = socialRank;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", ingredients=" + ingredients +
                ", recipeId='" + recipeId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", socialRank=" + socialRank +
                '}';
    }

    // Parcelable implementation
    protected Recipe(Parcel in) {
        title = in.readString();
        publisher = in.readString();
        ingredients = in.createStringArrayList();
        recipeId = in.readString();
        imageUrl = in.readString();
        if (in.readByte() == 0) {
            socialRank = null;
        } else {
            socialRank = in.readDouble();
        }
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(publisher);
        dest.writeStringList(ingredients);
        dest.writeString(recipeId);
        dest.writeString(imageUrl);
        if (socialRank == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(socialRank);
        }
    }
}
