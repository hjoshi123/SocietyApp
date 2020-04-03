package com.hacks.societyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.hacks.societyapp.model.CartItems;
import com.hacks.societyapp.model.Items;
import com.hacks.societyapp.retrofitapi.SocietyAPI;
import com.hacks.societyapp.retrofitclient.SocietyClient;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private NavController mNavController;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SocietyAPI societyAPI = SocietyClient.getClient(this)
                .create(SocietyAPI.class);
        societyAPI.getCart()
            .enqueue(new Callback<ArrayList<CartItems>>() {
                @Override
                public void onResponse(@NotNull Call<ArrayList<CartItems>> call,
                                       @NotNull Response<ArrayList<CartItems>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<CartItems> items = response.body();
                        if (!items.isEmpty()) {
                            SharedPreferences prefs = getSharedPreferences("cartQuantity",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = prefs.edit();

                            for (CartItems item: items) {
                                edit.putInt(item.getItemCode(), item.getCartQuantity());
                            }
                            edit.apply();
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ArrayList<CartItems>> call, @NotNull Throwable t) {

                }
            });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment),
                mDrawerLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        mDrawerLayout.closeDrawers();
        int id = item.getItemId();

        switch (id) {
            case R.id.grocery:
                mNavController.navigate(R.id.groceryFragment);
                break;
            case R.id.toileteries:
                mNavController.navigate(R.id.toileteriesFragment);
                break;
        }
        return true;
    }

    private void setupNavigation() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigationView);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mDrawerLayout);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
        mNavigationView.setNavigationItemSelectedListener(this);

        View headerView = mNavigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.username);
        TextView email = headerView.findViewById(R.id.email);
        AppCompatImageView profile = headerView.findViewById(R.id.appCompatImageView);
    }

}
