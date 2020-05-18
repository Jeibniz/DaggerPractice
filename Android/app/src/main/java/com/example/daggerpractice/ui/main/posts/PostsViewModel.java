package com.example.daggerpractice.ui.main.posts;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.daggerpractice.SessionManager;
import com.example.daggerpractice.network.main.MainApi;
import javax.inject.Inject;

public class PostsViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";

    // Injected
    private final SessionManager mSessionManager;
    private final MainApi mMainApi;

    @Inject
    public PostsViewModel(SessionManager sessionManager, MainApi mainApi) {
        mSessionManager = sessionManager;
        mMainApi = mainApi;

        Log.d(TAG, "PostsViewModel: Working =D");
    }
}
