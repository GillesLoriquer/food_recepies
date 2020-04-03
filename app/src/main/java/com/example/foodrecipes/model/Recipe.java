
package com.example.foodrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {

    /**
     * -------------------------------- ATTRIBUTES
     */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "recipe_id")
    @SerializedName("recipe_id")
    private String recipeId;

    private String title;

    private String publisher;

    private String[] ingredients;

    @ColumnInfo(name = "image_url")
    @SerializedName("image_url")
    private String imageUrl;

    @ColumnInfo(name = "social_rank")
    @SerializedName("social_rank")
    private Double socialRank;

    private int timestamp;

    /**
     * -------------------------------- CONSTRUCTORS
     */
    public Recipe() {
        this.socialRank = 0.0D;
    }

    public Recipe(@NonNull String recipeId, String title, String publisher, String[] ingredients, String imageUrl, Double socialRank, int timestamp) {
        this.recipeId = recipeId;
        this.title = title;
        this.publisher = publisher;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
        this.socialRank = socialRank;
        this.timestamp = timestamp;
    }

    /**
     * -------------------------------- GETTERS
     */
    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Double getSocialRank() {
        return socialRank;
    }

    public int getTimestamp() {
        return timestamp;
    }

    /**
     * -------------------------------- SETTERS
     */
    public void setRecipeId(@NonNull String recipeId) {
        this.recipeId = recipeId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSocialRank(Double socialRank) {
        this.socialRank = socialRank;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * -------------------------------- BUILT-IN METHODES
     */
    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId='" + recipeId + '\'' +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", ingredients=" + ingredients +
                ", imageUrl='" + imageUrl + '\'' +
                ", socialRank=" + socialRank +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * -------------------------------- PARCELABLE IMPLEMENTATION
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeId);
        dest.writeString(title);
        dest.writeString(publisher);
        dest.writeArray(ingredients);
        dest.writeString(imageUrl);
        if (socialRank == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(socialRank);
        }
        dest.writeInt(timestamp);
    }

    protected Recipe(Parcel in) {
        recipeId = in.readString();
        title = in.readString();
        publisher = in.readString();
        ingredients = in.createStringArray();
        imageUrl = in.readString();
        if (in.readByte() == 0) {
            socialRank = null;
        } else {
            socialRank = in.readDouble();
        }
        timestamp = in.readInt();
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

}
