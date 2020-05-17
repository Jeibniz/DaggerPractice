package com.example.daggerpractice.ui.auth;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.RequestManager;
import com.example.daggerpractice.R;
import com.example.daggerpractice.viewmodels.ViewModelProviderFactory;
import dagger.android.support.DaggerAppCompatActivity;
import javax.inject.Inject;

public class AuthActivity extends DaggerAppCompatActivity {

    private static final String TAG = "AuthActivity";

    private AuthViewModel mViewModel;

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

        mViewModel = ViewModelProviders.of(this, mProviderFactory).get(AuthViewModel.class);

        setLogo();
    }

    private void setLogo() {
        mRequestManager.
                load(mLogo)
                .into((ImageView) findViewById(R.id.login_logo));
    }
}
