package com.hacks.societyapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.hacks.societyapp.R;
import com.hacks.societyapp.model.CartItems;
import com.hacks.societyapp.model.Items;
import com.hacks.societyapp.model.UserDetails;
import com.hacks.societyapp.retrofitapi.SocietyAPI;
import com.hacks.societyapp.retrofitclient.SocietyClient;
import com.hacks.societyapp.service.CheckOrderStatusService;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private NavController mNavController;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private SocietyAPI mSocietyAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSocietyAPI = SocietyClient.getClient(this)
            .create(SocietyAPI.class);
        setupNavigation();
        startService(new Intent(this, CheckOrderStatusService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        storeCartQuantity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        storeCartQuantity();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.cart:
                startActivity(new Intent(MainActivity.this, ViewCartActivity.class));
                return true;
            case R.id.view_order:
                startActivity(new Intent(MainActivity.this, ViewOrdersActivity.class));
                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                return true;
            case R.id.toileteries:
                mNavController.navigate(R.id.toileteriesFragment);
                return true;
            case R.id.beverages:
                mNavController.navigate(R.id.beveragesFragment);
                return true;
            case R.id.others:
                mNavController.navigate(R.id.others_fragment);
                return true;
        }
        return false;
    }

    private void storeCartQuantity() {
        mSocietyAPI.getCart()
                .enqueue(new Callback<ArrayList<CartItems>>() {
                    @Override
                    public void onResponse(@NotNull Call<ArrayList<CartItems>> call,
                                           @NotNull Response<ArrayList<CartItems>> response) {
                        if (response.isSuccessful()) {
                            SharedPreferences prefs = getSharedPreferences("cartQuantity",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = prefs.edit();
                            ArrayList<CartItems> items = response.body();
                            if (!items.isEmpty()) {
                                for (CartItems item: items) {
                                    edit.putInt(item.getItemCode(), item.getCartQuantity());
                                }
                                edit.commit();
                            } else {
                                edit.clear();
                                edit.commit();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ArrayList<CartItems>> call, @NotNull Throwable t) {

                    }
                });
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

        View footer = mNavigationView.findViewById(R.id.footer);
        TextView viewOrder = footer.findViewById(R.id.view_orders);

        viewOrder.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ViewOrdersActivity.class));
        });

        mSocietyAPI.getUserDetails()
            .enqueue(new Callback<UserDetails>() {
                @Override
                public void onResponse(@NotNull Call<UserDetails> call,
                                       @NotNull Response<UserDetails> response) {
                    if (response.isSuccessful()) {
                        UserDetails user = response.body();

                        userName.setText(user.getUsername());
                        email.setText(user.getEmail());
                    }
                }

                @Override
                public void onFailure(Call<UserDetails> call, Throwable t) {

                }
            });
    }

    private void logout() {
        SharedPreferences pref = getSharedPreferences("cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        HashSet<String> cookie = new HashSet<>();
        editor.putStringSet("cookies", cookie).commit();

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
