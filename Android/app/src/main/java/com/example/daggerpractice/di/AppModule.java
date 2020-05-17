package com.example.daggerpractice.di;

import dagger.Provides;

public class AppModule {

    @Provides
    static String someString() {
        return "Injected string!!";
    }
}
