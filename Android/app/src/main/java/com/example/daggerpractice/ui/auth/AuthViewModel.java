package com.example.daggerpractice.ui.auth;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.example.daggerpractice.SessionManager;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.network.auth.AuthApi;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";
    private static final int ERROR_USER_ID = -1;
    private static final int CALL_TIMEOUT_SECONDS = 10;
    private static final String ERROR_AUTHENTICATE = "Could not authenticate";

    private final AuthApi mAuthApi;
    private SessionManager mSessionManager;
    //private MediatorLiveData<AuthResource<User>> mAuthUser = new MediatorLiveData<>();

    @Inject
    public AuthViewModel(AuthApi authApi, SessionManager sessionManager) {
        mAuthApi = authApi;
        mSessionManager = sessionManager;
    }

    public void authenticateWithId(int userId) {
        Log.d(TAG, "authenticateWithId: Attempting to login");
        mSessionManager.authenticateWithId(queryUserId(userId));
    }

    public LiveData<AuthResource<User>> observeAuthState() {
        return mSessionManager.getAuthUser();
    }

    private LiveData<AuthResource<User>> queryUserId(int userId) {
        return LiveDataReactiveStreams.fromPublisher(
                mAuthApi.getUser(userId)
                        // Run instead of calling onError
                        .onErrorReturn(new Function<Throwable, User>() {
                            @Override
                            public User apply(final Throwable throwable) throws Exception {
                                return onAuthenticateWithIdError();
                            }
                        })
                        // Wrap incoming user object in AuthResource.
                        .map(new Function<User, AuthResource<User>>() {
                            @Override
                            public AuthResource<User> apply(final User user) throws Exception {
                                return onAuthenticateWithIdMap(user);
                            }
                        }).timeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
        );
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
