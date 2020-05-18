package com.example.daggerpractice.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.daggerpractice.BaseActivity;
import com.example.daggerpractice.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends BaseActivity implements OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private NavController mNavController;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initNavigation();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mDrawerLayout);
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
                return true; //Consume click event
            case android.R.id.home: // Drawer icon or back arrow
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return true; //Consume click event
                }
                return false; // Do not consume click event if drawer is closed.
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.main_drawer_nav_profile:

                // Options to clear back-stack when navigated to profile fragment.
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.main, true)
                        .build();

                mNavController.navigate(
                        R.id.navigation_fragment_profile,
                        null,
                        navOptions
                );
                break;
            case R.id.main_drawer_nav_posts:
                // Check if we are currently at the fragment we want to navigate to
                if(isValidDestionation(R.id.main_drawer_nav_posts)){
                    mNavController.navigate(R.id.navigation_fragment_posts);
                }
                break;
        }
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void initViews() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.activity_main_nav_view);
    }

    private void initNavigation() {

        mNavController =
                Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mDrawerLayout);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void onMenuLogoutClick() {
        sessionManager.logOut();
    }

    private boolean isValidDestionation(final int destination) {
        return mNavController.getCurrentDestination().getId() != destination;
    }
}
