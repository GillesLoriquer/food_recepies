package com.example.foodrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrecipes.adapter.OnRecipeListener;
import com.example.foodrecipes.adapter.RecipeRecyclerAdapter;
import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.util.Testing;
import com.example.foodrecipes.util.VerticalSpacingItemDecorator;
import com.example.foodrecipes.viewmodel.RecipeListViewModel;
import com.example.foodrecipes.viewmodel.RecipeListViewModelFactory;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    /**
     * -------------------------------- VARIABLES
     */
    private static final String TAG = "RecipeListActivity";

    private RecipeListViewModel mRecipeListViewModel;

    private RecyclerView mRecyclerView;

    private RecipeRecyclerAdapter mRecyclerAdapter;

    private SearchView mSearchView;

    /**
     * -------------------------------- BUILT-IN METHODS
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mSearchView = findViewById(R.id.search_view);

        mRecyclerView = findViewById(R.id.recipe_list);

        mRecipeListViewModel = new ViewModelProvider(
                this,
                new RecipeListViewModelFactory(this.getApplication())).get(RecipeListViewModel.class);

        initRecyclerView();

        initSearchView();

        setSupportActionBar(findViewById(R.id.toolbar));

        subscribeObservers();
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

    @Override
    public void onRecipeClick(int position) {
        Recipe recipe = mRecyclerAdapter.getSelectedRecipe(position);
        if (recipe != null) {
            Intent intent = new Intent(this, RecipeActivity.class);
            intent.putExtra(RecipeActivity.RECIPE, recipe);
            startActivity(intent);
        }
    }

    @Override
    public void onCategoryClick(String category) {
        searchRecipesApi(category);
    }

    /**
     * -------------------------------- METHODS
     */
    private void initRecyclerView() {
        mRecyclerAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator =
                new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(verticalSpacingItemDecorator);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecipesApi(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void subscribeObservers() {
        mRecipeListViewModel.getViewState().observe(this, viewState -> {
            if (viewState != null) {
                switch (viewState) {
                    case CATEGORIES: {
                        displayCategories();
                        break;
                    }
                    case RECIPES: {
                        // TODO : recipes will show automatically from another observer
                        break;
                    }
                }
            }
        });
        mRecipeListViewModel.getRecipes().observe(this, listResource -> {
            if (listResource != null) {
                Log.d(TAG, "subscribeObservers: ----------------------- status " + listResource.status);

                if (listResource.data != null) {
                    Testing.printRecepies(listResource.data, "data");
                    this.mRecyclerAdapter.setRecipeList(listResource.data);
                }
            }
        });
    }

    private void searchRecipesApi(String query) {
        mRecipeListViewModel.searchRecipesApi(query, 1);
    }

    private void displayCategories() {
        mRecyclerAdapter.displayCategories();
    }
}
