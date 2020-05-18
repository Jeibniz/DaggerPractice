package com.example.daggerpractice.ui.main.posts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.daggerpractice.R;
import com.example.daggerpractice.models.Post;
import com.example.daggerpractice.ui.main.Resource;
import com.example.daggerpractice.ui.main.Resource.Status;
import com.example.daggerpractice.util.VerticalSpacingItemDecoration;
import com.example.daggerpractice.viewmodels.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;
import java.util.List;
import javax.inject.Inject;

public class PostsFragment extends DaggerFragment {

    private static final String TAG = "PostsFragment";
    private PostsViewModel mViewModel;
    private RecyclerView mRecyclerView;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    PostsRecyclerAdapter adapter;

    @Inject
    LinearLayoutManager layoutManager;

    @Inject
    VerticalSpacingItemDecoration itemDecoration;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(PostsViewModel.class);
        initRecycleView(view);
        subscribeObservers();
    }
    
    private void subscribeObservers(){
        mViewModel.observePosts().removeObservers(getViewLifecycleOwner());
        mViewModel.observePosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Post>>>() {
            @Override
            public void onChanged(final Resource<List<Post>> listResource) {
                onPostsChanged(listResource);
            }
        });
    }

    private void onPostsChanged(final Resource<List<Post>> listResource) {
        if(null == listResource){
            return;
        }

        switch (listResource.status) {
            case LOADING:
                Log.d(TAG, "onPostsChanged: Loading ...");
                break;
            case SUCCESS:
                Log.d(TAG, "onPostsChanged: got posts ...");
                adapter.setPosts(listResource.data);
                break;
            case ERROR:
                Log.e(TAG, "onPostsChanged: error: " + listResource.message);
                break;
        }
    }

    private void initRecycleView(View view){
        mRecyclerView = view.findViewById(R.id.fragment_posts_recycler_view);
        if(layoutManager != mRecyclerView.getLayoutManager()){ // Only add it the first time
            mRecyclerView.setLayoutManager(layoutManager);
        }
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove LayoutManager from the to be destroyed RecyclerView to be able to add it to other recyclerViews.
        mRecyclerView.setLayoutManager(null);
    }
}
