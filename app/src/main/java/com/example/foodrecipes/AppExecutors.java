package com.example.foodrecipes;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    /**
     * -------------------------------- VARIABLES
     */
    private static AppExecutors instance;

    private final Executor mDiskIO = Executors.newSingleThreadExecutor();

    private final Executor mMainThreadExecutor = new MainThreadExecutor();


    /**
     * -------------------------------- GETTERS
     */
    public Executor getDiskIO() {
        return mDiskIO;
    }

    public Executor getMainThreadExecutor() {
        return mMainThreadExecutor;
    }

    /**
     * -------------------------------- METHODS
     */
    public static AppExecutors getInstance() {
        if (instance == null) {
            instance = new AppExecutors();
        }
        return instance;
    }

    /**
     * -------------------------------- INNER CLASS
     */
    private class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
