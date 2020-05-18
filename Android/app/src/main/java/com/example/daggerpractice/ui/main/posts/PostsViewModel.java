package com.example.daggerpractice.ui.main.posts;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.example.daggerpractice.SessionManager;
import com.example.daggerpractice.models.Post;
import com.example.daggerpractice.network.main.MainApi;
import com.example.daggerpractice.ui.main.Resource;
import com.google.gson.annotations.Expose;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class PostsViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";
    private static final int ERROR_POST_ID = -1;

    // Injected
    private final SessionManager mSessionManager;
    private final MainApi mMainApi;

    MediatorLiveData<Resource<List<Post>>> mPosts;

    @Inject
    public PostsViewModel(SessionManager sessionManager, MainApi mainApi) {
        mSessionManager = sessionManager;
        mMainApi = mainApi;

        Log.d(TAG, "PostsViewModel: Working =D");
    }

    public LiveData<Resource<List<Post>>> observePosts() {
        if(null == mPosts){
           initPosts();
        }
        return mPosts;
    }

    private void initPosts(){
        mPosts = new MediatorLiveData<>();
        mPosts.setValue(Resource.loading((List<Post>) null));

        final  LiveData<Resource<List<Post>>> source = LiveDataReactiveStreams.fromPublisher(
                mMainApi.getPostsFromUser(mSessionManager.getAuthUser().getValue().data.getId())
                        .onErrorReturn(new Function<Throwable, List<Post>>() {
                            @Override
                            public List<Post> apply(final Throwable throwable) throws Exception {
                                return observePostsOnError(throwable);
                            }
                        }).map(new Function<List<Post>, Resource<List<Post>>>() {
                    @Override
                    public Resource<List<Post>> apply(final List<Post> posts) throws Exception {
                        return observePostsMap(posts);
                    }
                }).subscribeOn(Schedulers.io())
        );

        mPosts.addSource(source, new Observer<Resource<List<Post>>>() {
            @Override
            public void onChanged(final Resource<List<Post>> listResource) {
                onPostsChanged(source, listResource);
            }
        });
    }

    private List<Post> observePostsOnError(final Throwable throwable){
        Log.e(TAG, "apply: onErrorReturn", throwable);
        Post post = new Post();
        post.setId(ERROR_POST_ID);
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        return posts;
    }

    private Resource<List<Post>> observePostsMap(final List<Post> posts) {
        if(posts.size() < 0 && ERROR_POST_ID == posts.get(0).getId()){
            return Resource.error("Something went wrong in posts API request", null);
        }
        return Resource.success(posts);
    }

    private void onPostsChanged(final LiveData<Resource<List<Post>>> source,
            final Resource<List<Post>> listResource) {
        mPosts.setValue(listResource);
        mPosts.removeSource(source);
    }
}
