package com.example.daggerpractice.di;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    static String someString() {
        return "Injected string!!";
    }
}
