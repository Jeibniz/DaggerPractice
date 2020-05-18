package com.example.daggerpractice.ui.main.posts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.example.daggerpractice.R;
import com.example.daggerpractice.viewmodels.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;
import javax.inject.Inject;

public class PostsFragment extends DaggerFragment {

    private static final String TAG = "PostsFragment";
    private PostsViewModel mViewModel;
    private RecyclerView mRecyclerView;

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
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.fragment_posts_recycler_view);
        mViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(PostsViewModel.class);
    }
}
