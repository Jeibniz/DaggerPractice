package com.example.daggerpractice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.ui.auth.AuthActivity;
import com.example.daggerpractice.ui.auth.AuthResource;
import dagger.android.support.DaggerAppCompatActivity;
import javax.inject.Inject;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Inject
    public SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeObservers();
    }

    private void subscribeObservers(){
        sessionManager.getAuthUser().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(final AuthResource<User> userAuthResource) {
                onAuthStateChanged(userAuthResource);
            }
        });
    }

    private void onAuthStateChanged(final AuthResource<User> userAuthResource){
        if(null == userAuthResource){
            return;
        }
        switch (userAuthResource.status) {
            case LOADING:
                break;
            case AUTHENTICATED:
                Log.d(TAG, "onUserResourceChanged: Login success: " + userAuthResource.data.getUsername());
                break;
            case ERROR:
                Log.e(TAG, "onAuthStateChanged: " + userAuthResource.message);
                Toast.makeText(this, R.string.auth_login_toast_invalid, Toast.LENGTH_LONG).show();
                break;
            case NOT_AUTHENTICATED:
                // Redirect to login screen if suddenly not authenticated.
                navLoginScreen();
                break;
        }
    }

    private void navLoginScreen() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
