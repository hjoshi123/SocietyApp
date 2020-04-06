package com.hacks.societyapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hacks.societyapp.R;
import com.hacks.societyapp.Utils.ItemsAdapter;
import com.hacks.societyapp.model.Authentication;
import com.hacks.societyapp.model.CartItems;
import com.hacks.societyapp.model.Items;
import com.hacks.societyapp.retrofitapi.SocietyAPI;
import com.hacks.societyapp.retrofitclient.SocietyClient;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ViewCartActivity extends AppCompatActivity implements View.OnClickListener {
    private SocietyAPI mSocietyAPI;
    private RecyclerView mViewCartRecyclerView;
    private TextView mTotalPrice;
    private MaterialProgressBar mViewCartProgress;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        mViewCartRecyclerView = findViewById(R.id.view_cart_recyclerview);
        mTotalPrice = findViewById(R.id.total_price);
        mViewCartProgress = findViewById(R.id.view_cart_progress);

        getSupportActionBar().setDisplayOptions
            (ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button placeOrderButton = findViewById(R.id.place_order);
        placeOrderButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getCurrentCart();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.place_order) {

            new FancyAlertDialog.Builder(this)
                .setTitle("Confirm Place Order")
                .setBackgroundColor(Color.parseColor("#303F9F"))
                .setMessage("Do you want to place your order? Kindly confirm the cart and order value..")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))
                .setPositiveBtnText("Confirm")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_done_white_48dp, Icon.Visible)
                .OnPositiveClicked(() -> mSocietyAPI.placeOrder()
                        .enqueue(new Callback<Authentication>() {
                            @Override
                            public void onResponse(@NonNull Call<Authentication> call,
                                                   @NonNull Response<Authentication> response) {
                                Authentication auth = response.body();

                                if (auth.getSuccessMsg().equals("true")) {
                                    Toast.makeText(ViewCartActivity.this,
                                            "Order Placed Successfully",
                                            Toast.LENGTH_SHORT).show();
                                    SharedPreferences prefs = getSharedPreferences("cartQuantity",
                                            Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.clear();
                                    editor.commit();
                                    finish();
                                } else {
                                    Toast.makeText(ViewCartActivity.this,
                                            "Order Not placed due to " + auth.getErrorCode(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Authentication> call, Throwable t) {

                            }
                        }))
                .OnNegativeClicked(() -> {

                })
                .build();
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

    private void getCurrentCart() {
        mSocietyAPI = SocietyClient.getClient(this)
                .create(SocietyAPI.class);

        mSocietyAPI.getCart()
            .enqueue(new Callback<ArrayList<CartItems>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<CartItems>> call,
                                       @NonNull Response<ArrayList<CartItems>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<CartItems> items = response.body();

                        ArrayList<Items> itemForDisplay = new ArrayList<>();

                        double totalPrice = 0;
                        for(CartItems item: items) {
                            itemForDisplay.add(new Items(item.getCategory(),
                                    item.getRate(),
                                    item.getImageUrl(),
                                    item.getItemDescription(),
                                    item.getGstRate(),
                                    item.getMeasurementUnit(),
                                    item.getMaxAllowed(),
                                    item.getItemCode(),
                                    item.getQuantity()));

                            totalPrice += (item.getCartQuantity() * item.getRate());
                        }
                        mTotalPrice.setText("Rs " + totalPrice);

                        ItemsAdapter adapter = new ItemsAdapter(itemForDisplay,
                                ViewCartActivity.this);
                        mViewCartRecyclerView.setAdapter(adapter);
                        mViewCartRecyclerView.setLayoutManager(
                                new LinearLayoutManager(ViewCartActivity.this));
                        mViewCartProgress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<CartItems>> call,
                                      @NonNull Throwable t) {
                    mViewCartProgress.setVisibility(View.GONE);
                    Timber.d(t);
                }
            });
    }
}
