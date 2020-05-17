package com.example.daggerpractice;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.ui.auth.AuthResource;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private static final String TAG = "SessionManager";

    private MediatorLiveData<AuthResource<User>> mCachedUser = new MediatorLiveData<>();

    @Inject
    public SessionManager() {
    }

    private void authenticateWithId(final LiveData<AuthResource<User>> source){
        // Stop if no cached user
        if(null == mCachedUser){
            return;
        }
        mCachedUser.setValue(AuthResource.loading((User) null));
        mCachedUser.addSource(source, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(final AuthResource<User> userAuthResource) {
                onAuthenticateWithIdSourceChanged(source, userAuthResource);
            }
        });

    }
    
    private void logOut(){
        Log.d(TAG, "logOut: logging out ...");
        mCachedUser.setValue(AuthResource.<User>logout());
    }
    
    private LiveData<AuthResource<User>> getAuthUser(){
        return mCachedUser;
    }

    private void onAuthenticateWithIdSourceChanged(final LiveData<AuthResource<User>> source,
            final AuthResource<User> userAuthResource) {
        mCachedUser.setValue(userAuthResource);
        mCachedUser.removeSource(source);
    }
}
