package com.hacks.societyapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hacks.societyapp.R;
import com.hacks.societyapp.Utils.adapter.OrderDetailsAdapter;
import com.hacks.societyapp.model.OrderResponse;
import com.hacks.societyapp.retrofitapi.SocietyAPI;
import com.hacks.societyapp.retrofitclient.SocietyClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    private RecyclerView mViewDetailsRecyclerView;
    private LinearLayout mItemLayout;
    private LinearLayout mDetailsLayout;
    private ProgressBar mProgressBar;
    private TextView mTotalPrice;
    private TextView mOrderId;
    private SocietyAPI mSocietyAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        getSupportActionBar().setTitle("Your Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mItemLayout = findViewById(R.id.ll_item_products);
        mDetailsLayout = findViewById(R.id.ll_item);

        mProgressBar = findViewById(R.id.progressBarOrderDetails);
        mViewDetailsRecyclerView = mItemLayout.findViewById(R.id.recyclerview_item_orders);
        mTotalPrice = findViewById(R.id.total_amount);
        mOrderId = findViewById(R.id.orderid);

        int position = getIntent().getIntExtra("position", -1);
        if (position != -1) {
            mProgressBar.setVisibility(View.VISIBLE);
            getItemsInOrder(position);
        }
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

    private void getItemsInOrder(int position) {
        mSocietyAPI = SocietyClient.getClient(this)
                .create(SocietyAPI.class);

        mSocietyAPI.getOrder()
                .enqueue(new Callback<ArrayList<OrderResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<OrderResponse>> call,
                                           @NonNull Response<ArrayList<OrderResponse>> response) {
                        if (response.isSuccessful()) {
                            ArrayList<OrderResponse> orders = response.body();

                            OrderResponse order = orders.get(position);

                            ArrayList<OrderResponse.Item> items = (ArrayList<OrderResponse.Item>)
                                    order.getItems();

                            mItemLayout.setVisibility(View.VISIBLE);

                            OrderDetailsAdapter adapter = new OrderDetailsAdapter(items);
                            mViewDetailsRecyclerView.setAdapter(adapter);
                            mViewDetailsRecyclerView.setLayoutManager
                                    (new LinearLayoutManager(OrderDetailsActivity.this));

                            mProgressBar.setVisibility(View.GONE);

                            mDetailsLayout.setVisibility(View.VISIBLE);
                            mTotalPrice.setText("₹ " + order.getTotalValue());
                            mOrderId.setText(order.getOrderId().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<OrderResponse>> call, Throwable t) {

                    }
                });
    }
}
