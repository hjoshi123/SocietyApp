package com.hacks.societyapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.hacks.societyapp.R;
import com.hacks.societyapp.Utils.adapter.ViewOrdersAdapter;
import com.hacks.societyapp.model.OrderResponse;
import com.hacks.societyapp.retrofitapi.SocietyAPI;
import com.hacks.societyapp.retrofitclient.SocietyClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrdersActivity extends AppCompatActivity {
    private SocietyAPI mSocietyAPI;
    private ConstraintLayout mEmptyLayout;
    private ProgressBar mProgressBar;
    private RecyclerView mViewOrdersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        getSupportActionBar().setTitle("Your Previous Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmptyLayout = findViewById(R.id.ll_empty);
        mProgressBar = findViewById(R.id.progressBarViewOrder);
        mViewOrdersRecyclerView = findViewById(R.id.recyclerview_order_details);

        mSocietyAPI = SocietyClient.getClient(this)
            .create(SocietyAPI.class);

        mProgressBar.setVisibility(View.VISIBLE);
        getPreviousOrders();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPreviousOrders() {
        mSocietyAPI.getOrder()
            .enqueue(new Callback<ArrayList<OrderResponse>>() {
                @Override
                public void onResponse(@NotNull Call<ArrayList<OrderResponse>> call,
                                       @NotNull Response<ArrayList<OrderResponse>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<OrderResponse> orders = response.body();

                        if (orders.isEmpty()) {
                            mEmptyLayout.setVisibility(View.VISIBLE);
                        } else {
                            ViewOrdersAdapter adapter = new ViewOrdersAdapter(ViewOrdersActivity.this,
                                    orders);
                            mViewOrdersRecyclerView.setAdapter(adapter);
                            mViewOrdersRecyclerView.setLayoutManager(
                                    new LinearLayoutManager(ViewOrdersActivity.this));
                            mProgressBar.setVisibility(View.GONE);
                        }
                    } else {
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<OrderResponse>> call, Throwable t) {

                }
            });
    }
}
