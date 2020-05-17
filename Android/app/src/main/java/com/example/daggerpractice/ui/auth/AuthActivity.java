package com.example.daggerpractice.ui.auth;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.RequestManager;
import com.example.daggerpractice.R;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.viewmodels.ViewModelProviderFactory;
import dagger.android.support.DaggerAppCompatActivity;
import javax.inject.Inject;

public class AuthActivity extends DaggerAppCompatActivity {

    private static final String TAG = "AuthActivity";

    private AuthViewModel mViewModel;

    private EditText mEditTextUserId;
    private ProgressBar mProgressBar;

    @Inject
    ViewModelProviderFactory mProviderFactory;

    @Inject
    Drawable mLogo;

    @Inject
    RequestManager mRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initUiComponents();

        mViewModel = ViewModelProviders.of(this, mProviderFactory).get(AuthViewModel.class);

        subscribeObservers();
    }

    private void initUiComponents() {
        mEditTextUserId = findViewById(R.id.user_id_input);
        mProgressBar = findViewById(R.id.progress_bar);
        findViewById(R.id.login_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                attemptLogin();
            }
        });
        setLogo();
    }

    private void setLogo() {
        mRequestManager.
                load(mLogo)
                .into((ImageView) findViewById(R.id.login_logo));
    }

    private void attemptLogin(){
        String input = mEditTextUserId.getText().toString();
        // Show a toast if no input or if input is invalid.
        if(TextUtils.isEmpty(input)){
            Toast.makeText(this, R.string.auth_login_toast_empty, Toast.LENGTH_LONG).show();
            return;
        }
        if(!TextUtils.isDigitsOnly(input)){
            Toast.makeText(this, R.string.auth_login_toast_invalid, Toast.LENGTH_LONG).show();
            return;
        }
        mViewModel.authenticateWithId(Integer.parseInt(mEditTextUserId.getText().toString()));
    }

    private void subscribeObservers() {
        mViewModel.observeUser().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(final AuthResource<User> userAuthResource) {
                onUserResourceChanged(userAuthResource);
            }
        });
    }

    private void onUserResourceChanged(final AuthResource<User> userAuthResource){
        if(null == userAuthResource){
            return;
        }
        switch (userAuthResource.status) {
            case LOADING:
                showProgressBar(true);
                break;
            case AUTHENTICATED:
                showProgressBar(false);
                //TODO: replace with actual action
                Log.d(TAG, "onUserResourceChanged: Login success: " + userAuthResource.data.getUsername());
                break;
            case ERROR:
                Toast.makeText(this, R.string.auth_login_toast_invalid, Toast.LENGTH_LONG).show();
                showProgressBar(false);
                break;
            case NOT_AUTHENTICATED:
                showProgressBar(false);
                break;
        }
    }
    
    private void showProgressBar(boolean isVisible){
        if(isVisible){
            mProgressBar.setVisibility(View.VISIBLE);
        } else{
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
