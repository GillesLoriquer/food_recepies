package com.example.foodrecipes.util;

import android.util.Log;

import com.example.foodrecipes.model.Recipe;

import java.util.List;

public class Testing {

    public static void printRecepies(List<Recipe> list, String tag) {
        if (list != null) {
            for (Recipe r : list) {
                Log.d(tag, r.toString());
            }
        }
    }
}