package com.example.daggerpractice.ui.auth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
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
import com.example.daggerpractice.ui.main.MainActivity;
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
        mEditTextUserId.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(final View view, final int i, final KeyEvent keyEvent) {
                return onKeyUserText(i, keyEvent);
            }
        });
        mProgressBar = findViewById(R.id.progress_bar);
        findViewById(R.id.login_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                attemptLogin();
            }
        });
        setLogo();
    }

    private boolean onKeyUserText(final int keyCode, final KeyEvent keyEvent) {
            // If the event is a key-down event on the "enter" button
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                attemptLogin();
                return true;
            }
            return false;
    }

    private void setLogo() {
        mRequestManager.
                load(mLogo)
                .into((ImageView) findViewById(R.id.login_logo));
    }

    private void attemptLogin(){
        hideKeyboard();
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
        mViewModel.observeAuthState().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(final AuthResource<User> userAuthResource) {
                onAuthStateChanged(userAuthResource);
            }
        });
    }

    private void onLoginSuccess(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void onAuthStateChanged(final AuthResource<User> userAuthResource){
        if(null == userAuthResource){
            return;
        }
        switch (userAuthResource.status) {
            case LOADING:
                showProgressBar(true);
                break;
            case AUTHENTICATED:
                showProgressBar(false);
                Log.d(TAG, "onUserResourceChanged: Login success: " + userAuthResource.data.getUsername());
                onLoginSuccess();
                break;
            case ERROR:
                Log.e(TAG, "onAuthStateChanged: " + userAuthResource.message);
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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (null == view) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
