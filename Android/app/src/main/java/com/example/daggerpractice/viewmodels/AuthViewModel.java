package com.example.daggerpractice.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.daggerpractice.di.network.auth.AuthApi;
import javax.inject.Inject;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    private final AuthApi mAuthApi;

    @Inject
    public AuthViewModel(AuthApi authApi) {
        mAuthApi = authApi;
        
        Log.d(TAG, "AuthViewModel: viewModel working =D");
        Log.d(TAG, "AuthViewModel: authApi = null ? " + (mAuthApi == null));
    }
}
