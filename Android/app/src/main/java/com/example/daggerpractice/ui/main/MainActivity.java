package com.example.daggerpractice.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.daggerpractice.BaseActivity;
import com.example.daggerpractice.R;
import com.example.daggerpractice.ui.main.posts.PostsFragment;
import com.example.daggerpractice.ui.main.profile.ProfileFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testFragment();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_logout:
                onMenuLogoutClick();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onMenuLogoutClick() {
        sessionManager.logOut();
    }

    private void testFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new PostsFragment())
        .commit();

    }
}
