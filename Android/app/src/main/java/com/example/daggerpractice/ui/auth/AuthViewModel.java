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
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";
    private static final int ERROR_USER_ID = -1;
    private static final String ERROR_AUTHENTICATE = "Could not authenticate";
    private final AuthApi mAuthApi;

    private MediatorLiveData<AuthResource<User>> mAuthUser = new MediatorLiveData<>();

    @Inject
    public AuthViewModel(AuthApi authApi) {
        mAuthApi = authApi;
    }

    public void authenticateWithId(int userId) {
        // et null to tell UI that a request is in progress.
        mAuthUser.setValue(AuthResource.loading((User) null));
        final LiveData<AuthResource<User>> source = LiveDataReactiveStreams.fromPublisher(
                mAuthApi.getUser(userId)
                        .onErrorReturn(new Function<Throwable, User>() {
                            @Override
                            public User apply(final Throwable throwable) throws Exception {
                                return onAuthenticateWithIdError();
                            }
                        })
                        .map(new Function<User, AuthResource<User>>() {
                            @Override
                            public AuthResource<User> apply(final User user) throws Exception {
                                return onAuthenticateWithIdMap(user);
                            }
                        })
                        .subscribeOn(Schedulers.io())
        );

        mAuthUser.addSource(source, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(final AuthResource<User> user) {
                mAuthUser.setValue(user);
                mAuthUser.removeSource(source);
            }
        });
    }

    public LiveData<AuthResource<User>> observeUser() {
        return mAuthUser;
    }

    private User onAuthenticateWithIdError(){
        User errorUser = new User();
        errorUser.setId(ERROR_USER_ID);
        return errorUser;
    }

    private AuthResource<User> onAuthenticateWithIdMap(final User user){
        if(ERROR_USER_ID == user.getId()){
            return AuthResource.error(ERROR_AUTHENTICATE, (User) null);
        }
        return AuthResource.authenticated(user);
    }
}
