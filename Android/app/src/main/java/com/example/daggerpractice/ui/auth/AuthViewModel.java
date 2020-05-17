package com.example.daggerpractice.ui.auth;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.network.auth.AuthApi;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    private final AuthApi mAuthApi;

    private MediatorLiveData<User> mAuthUser = new MediatorLiveData<>();

    @Inject
    public AuthViewModel(AuthApi authApi) {
        mAuthApi = authApi;
    }

    public void authenticateWithId(int userId) {
        final LiveData<User> source = LiveDataReactiveStreams.fromPublisher(
                mAuthApi.getUser(userId)
                .subscribeOn(Schedulers.io())
        );

        mAuthUser.addSource(source, new Observer<User>() {
            @Override
            public void onChanged(final User user) {
                mAuthUser.setValue(user);
                mAuthUser.removeSource(source);
            }
        });
    }

    public LiveData<User> observableUser() {
        return mAuthUser;
    }
}
