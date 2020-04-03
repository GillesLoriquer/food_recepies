package com.example.foodrecipes.util;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.foodrecipes.AppExecutors;
import com.example.foodrecipes.network.response.ApiResponse;

// CacheObject: Type for the Resource data. (database cache)
// RequestObject: Type for the API response. (network request)
public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    /**
     * -------------------------------- VARIABLES
     */
    private static final String TAG = "NetworkBoundResource";

    private AppExecutors mAppExecutors;

    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();

    /**
     * -------------------------------- CONSTRUCTOR
     */
    public NetworkBoundResource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
        init();
    }

    /**
     * -------------------------------- METHODS
     */
    private void init() {
        // update LiveData for loading status
        results.setValue((Resource<CacheObject>) Resource.loading(null));

        // observe LiveData from local db
        final LiveData<CacheObject> dbSource = loadFromDb();

        results.addSource(dbSource, cacheObject -> {
            // remove observer from local db. Need to decide if read local db or network
            results.removeSource(dbSource);

            // get data from network if conditions in shouldFetch(boolean) are true
            if (shouldFetch(cacheObject)) {
                fetchFromNetwork(dbSource);
            } else { // otherwise read data from local db
                results.addSource(dbSource,
                        cacheObject2 -> setValue(Resource.success(cacheObject2)));
            }
        });
    }

    /**
     * 1) obserce local db
     * 2) if <condition/> then query the network
     * 3) stop observing the local db
     * 4) insert new data into local db
     * 5) begin observing local db again to see the refreshed data from network
     *
     * @param dbSource
     */
    private void fetchFromNetwork(final LiveData<CacheObject> dbSource) {
        Log.d(TAG, "fetchFromNetwork: called");

        // update LiveData for loading status
        results.addSource(dbSource, cacheObject -> setValue(Resource.loading(cacheObject)));

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();

        results.addSource(apiResponse, requestObjectApiResponse -> {
            results.removeSource(dbSource);
            results.removeSource(apiResponse);

            /**
             * 3 cases:
             *  1) ApiSuccessResponse
             *  2) ApiErrorResponse
             *  3) ApiEmptyResponse
             */
            if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {
                Log.d(TAG, "fetchFromNetwork: ApiSuccessResponse");

                mAppExecutors.getDiskIO().execute(() -> {
                    // save the response to the local db
                    saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse) requestObjectApiResponse));

                    mAppExecutors.getMainThreadExecutor().execute(()
                            -> results.addSource(loadFromDb(), cacheObject
                            -> setValue(Resource.success(cacheObject))));
                });
            } else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {
                Log.d(TAG, "fetchFromNetwork: ApiErrorResponse");

                results.addSource(dbSource, cacheObject
                        -> setValue(
                        Resource.error(((ApiResponse.ApiErrorResponse) requestObjectApiResponse).getErrorMessage(),
                                cacheObject)));
            } else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {
                Log.d(TAG, "fetchFromNetwork: ApiEmptyResponse");

                mAppExecutors.getMainThreadExecutor().execute(()
                        -> results.addSource(loadFromDb(), cacheObject
                        -> setValue(Resource.success(cacheObject))));
            }
        });
    }

    private CacheObject processResponse(ApiResponse.ApiSuccessResponse response) {
        return (CacheObject) response.getBody();
    }

    private void setValue(Resource<CacheObject> newValue) {
        if (results.getValue() != newValue) {
            results.setValue(newValue);
        }
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    // Called to get the cached data from the database.
    @NonNull
    @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData() {
        return results;
    }
}
