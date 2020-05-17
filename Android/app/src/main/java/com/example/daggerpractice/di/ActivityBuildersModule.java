package com.example.daggerpractice.di;

import com.example.daggerpractice.di.auth.AuthViewModelsModuel;
import com.example.daggerpractice.ui.auth.AuthActivity;
import com.example.daggerpractice.viewmodels.AuthViewModel;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
            modules = {AuthViewModelsModuel.class}
    )
    abstract AuthActivity contributeAuthActivity();
}
