package com.example.foodrecipes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrecipes.adapter.OnRecipeListener;
import com.example.foodrecipes.adapter.RecipeRecyclerAdapter;
import com.example.foodrecipes.util.VerticalSpacingItemDecorator;
import com.example.foodrecipes.viewmodel.RecipeListViewModel;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";

    private RecipeListViewModel mRecipeListViewModel;

    private RecyclerView mRecyclerView;

    private RecipeRecyclerAdapter mRecyclerAdapter;

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mSearchView = findViewById(R.id.search_view);

        mRecyclerView = findViewById(R.id.recipe_list);

        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        initRecyclerView();

        subscribeObservers();

        initSearchView();

        if (!mRecipeListViewModel.getIsViewingRecipes()) {
            displayCategories();
        }

        setSupportActionBar(findViewById(R.id.toolbar));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_categories) {
            displayCategories();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView() {
        mRecyclerAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator =
                new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(verticalSpacingItemDecorator);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, recipes -> {
            if (recipes != null && mRecipeListViewModel.getIsViewingRecipes()) {
                //Testing.printRecepies(recipes, "RecipesTest");
                mRecipeListViewModel.setPerformingQuery(false);
                mRecyclerAdapter.setRecipeList(recipes);
            }
        });
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRecyclerAdapter.displayLoading();
                mRecipeListViewModel.searchRecipesApi(query, 1);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {
        mRecyclerAdapter.displayLoading();
        mRecipeListViewModel.searchRecipesApi(category, 1);
        mSearchView.clearFocus();
    }

    private void displayCategories() {
        mRecipeListViewModel.setIsViewingRecipes(false);
        mRecyclerAdapter.displayCategories();
    }

    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.onBackPressed()) {
            super.onBackPressed();
        }
        displayCategories();
    }
}
