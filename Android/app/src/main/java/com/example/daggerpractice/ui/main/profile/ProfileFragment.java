package com.example.daggerpractice.ui.main.profile;

import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.daggerpractice.R;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.ui.auth.AuthResource;
import com.example.daggerpractice.viewmodels.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;
import javax.inject.Inject;

public class ProfileFragment extends DaggerFragment {

    private static final String TAG = "ProfileFragment";

    private ProfileViewModel mViewModel;
    private TextView mTextViewUsername, mTextViewEmail, mTextViewWebsite;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ProfileFragment was created.");
        setupViews(view);
        mViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(ProfileViewModel.class);
        subscribeObservers();
    }

    private void setupViews(final View view) {
        mTextViewUsername = view.findViewById(R.id.fragment_profile_username);
        mTextViewEmail= view.findViewById(R.id.fragment_profile_email);
        mTextViewWebsite = view.findViewById(R.id.fragment_profile_website);
    }

    private void subscribeObservers(){
        mViewModel.getAuthenticatedUser().removeObservers(getViewLifecycleOwner());
        mViewModel.getAuthenticatedUser().observe(getViewLifecycleOwner(), new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(final AuthResource<User> userAuthResource) {
                onAuthUserChanged(userAuthResource);
            }
        });
    }

    private void onAuthUserChanged(final AuthResource<User> userAuthResource) {
        if(null == userAuthResource){
            return;
        }
        switch (userAuthResource.status) {
            case AUTHENTICATED:
                setUserDetails(userAuthResource.data);
                break;
            case NOT_AUTHENTICATED:
                setErrorDetails(userAuthResource.message);
                break;
        }
    }

    private void setUserDetails(final User user) {
        mTextViewUsername.setText(user.getUsername());
        mTextViewEmail.setText(user.getEmail());
        mTextViewWebsite.setText(user.getWebsite());
    }

    private void setErrorDetails(final String message) {
        // A bit hacky but well...
        mTextViewUsername.setText(message);
        mTextViewEmail.setText("");
        mTextViewWebsite.setText("");
    }
}
