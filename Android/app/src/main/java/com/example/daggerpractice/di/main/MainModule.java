package com.example.daggerpractice.di.main;

import android.app.Application;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.daggerpractice.R;
import com.example.daggerpractice.network.main.MainApi;
import com.example.daggerpractice.ui.main.posts.PostsRecyclerAdapter;
import com.example.daggerpractice.util.VerticalSpacingItemDecoration;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    private static final String TAG = "MainModule";

    @MainScope
    @Provides
    static PostsRecyclerAdapter providePostRecyclerAdapter() {
        return new PostsRecyclerAdapter();
    }

    @MainScope
    @Provides
    static LinearLayoutManager provideLayoutManager(Application application) {
        return new LinearLayoutManager(application);
    }

    @MainScope
    @Provides
    static VerticalSpacingItemDecoration provideItemDecoraton(Application application) {
        int spacing =
               (int) application.getResources().getDimension(R.dimen.fragment_post_recycleview_vertical_spacing);
        return new VerticalSpacingItemDecoration(spacing);
    }

    @MainScope
    @Provides
    static MainApi provideMainApi(Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }
}
