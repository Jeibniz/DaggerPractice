package com.example.daggerpractice.di;

import com.example.daggerpractice.di.auth.AuthModule;
import com.example.daggerpractice.di.auth.AuthViewModelsModuel;
import com.example.daggerpractice.di.main.MainFragmentBuildersModule;
import com.example.daggerpractice.di.main.MainModule;
import com.example.daggerpractice.di.main.MainViewModulesModule;
import com.example.daggerpractice.ui.auth.AuthActivity;
import com.example.daggerpractice.ui.main.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
            modules = {
                    AuthViewModelsModuel.class,
                    AuthModule.class
            }
    )
    abstract AuthActivity contributeAuthActivity();

    @ContributesAndroidInjector(
            modules = {
                    MainFragmentBuildersModule.class,
                    MainViewModulesModule.class,
                    MainModule.class
            })
    abstract MainActivity contributeMainActivity();
}
