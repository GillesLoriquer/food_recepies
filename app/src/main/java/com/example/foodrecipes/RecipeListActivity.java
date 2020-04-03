package com.example.foodrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.foodrecipes.adapter.OnRecipeListener;
import com.example.foodrecipes.adapter.RecipeRecyclerAdapter;
import com.example.foodrecipes.model.Recipe;
import com.example.foodrecipes.viewmodel.RecipeListViewModel;
import com.example.foodrecipes.viewmodel.RecipeListViewModelFactory;

import static com.example.foodrecipes.viewmodel.RecipeListViewModel.QUERY_EXHAUSTED;

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
            mRecipeListViewModel.setViewState(RecipeListViewModel.ViewState.CATEGORIES);
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

    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.CATEGORIES) {
            super.onBackPressed();
        } else {
            mRecipeListViewModel.cancelSearchRequest();
            mRecipeListViewModel.setViewCategories();
        }
    }

    /**
     * -------------------------------- METHODS
     */
    private void initRecyclerView() {
        ViewPreloadSizeProvider<String> viewPreloadSizeProvider = new ViewPreloadSizeProvider<>();
        mRecyclerAdapter = new RecipeRecyclerAdapter(
                this,
                getRequestManager(),
                viewPreloadSizeProvider);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<>(
                Glide.with(this),
                mRecyclerAdapter,
                viewPreloadSizeProvider,
                30
        );
        mRecyclerView.addOnScrollListener(preloader);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // si l'utilisateur regarde des recettes && qu'il ne peut plus scroller verticalement (fin de liste)
                if (!(mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.CATEGORIES)
                        && !mRecyclerView.canScrollVertically(1)) {
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private RequestManager getRequestManager() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(requestOptions);
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
                        // recipes will show automatically from another observer
                        break;
                    }
                }
            }
        });
        mRecipeListViewModel.getRecipes().observe(this, listResource -> {
            if (listResource != null) {
                Log.d(TAG, "subscribeObservers: ----------------------- status " + listResource.status);

                if (listResource.data != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            if (mRecipeListViewModel.getPageNumber() > 1) {
                                mRecyclerAdapter.displayLoading();
                            } else {
                                mRecyclerAdapter.displayOnlyLoading();
                            }
                            break;
                        }
                        case ERROR: {
                            Log.e(TAG, "subscribeObservers: cannot refresh the cache.");
                            Log.e(TAG, "subscribeObservers: ERROR message: " + listResource.message);
                            Log.e(TAG, "subscribeObservers: status: ERROR, #recipes: " + listResource.data.size());
                            mRecyclerAdapter.hideLoading();
                            mRecyclerAdapter.setRecipeList(listResource.data);
                            Toast.makeText(RecipeListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();

                            if (listResource.message.equals(QUERY_EXHAUSTED)) {
                                mRecyclerAdapter.setQueryExhausted();
                            }
                            break;
                        }
                        case SUCCESS: {
                            Log.d(TAG, "subscribeObservers: cache has been refreshed.");
                            Log.d(TAG, "subscribeObservers: status: SUCCESS, #recipes: " + listResource.data.size());
                            mRecyclerAdapter.hideLoading();
                            mRecyclerAdapter.setRecipeList(listResource.data);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void searchRecipesApi(String query) {
        mRecyclerView.smoothScrollToPosition(0); // évite la selection en surbrillance d'un élément de la liste
        mRecipeListViewModel.searchRecipesApi(query, 1);
        mSearchView.clearFocus();
    }

    private void displayCategories() {
        mRecyclerAdapter.displayCategories();
    }
}
