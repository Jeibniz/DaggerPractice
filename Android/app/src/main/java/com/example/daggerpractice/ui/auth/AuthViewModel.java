package com.example.daggerpractice.ui.auth;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.network.auth.AuthApi;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    private final AuthApi mAuthApi;

    @Inject
    public AuthViewModel(AuthApi authApi) {
        mAuthApi = authApi;

        mAuthApi.getUser(2)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(final Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(final User user) {
                        Log.d(TAG, "onNext: " + user.getEmail());
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
        
        Log.d(TAG, "AuthViewModel: viewModel working =D");
        Log.d(TAG, "AuthViewModel: authApi = null ? " + (mAuthApi == null));
    }
}
