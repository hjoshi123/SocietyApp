package com.hacks.societyapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hacks.societyapp.R;
import com.hacks.societyapp.model.Authentication;
import com.hacks.societyapp.model.Items;
import com.hacks.societyapp.retrofitapi.SocietyAPI;
import com.hacks.societyapp.retrofitclient.SocietyClient;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private ArrayList<Items> mGroceryItems;
    private SocietyAPI mSocietyAPI;
    private Context mContext;

    public ItemsAdapter(ArrayList<Items> groceryItems, Context context) {
        this.mGroceryItems = groceryItems;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Items item = mGroceryItems.get(position);

        Picasso.get().load(item.getImageUrl()).into(holder.mProductImage);
        holder.mProductName.setText(item.getItemDescription());
        holder.mSellingPrice.setText(String.valueOf(item.getRate()));
        holder.mGSTRate.setText("  " +item.getGstRate() + " % GST");

        Timber.tag("Adapter").d("Entered here");
        SharedPreferences prefs = mContext.getSharedPreferences("cartQuantity",
                Context.MODE_PRIVATE);
        int quantity = prefs.getInt(item.getItemCode(), 0);
        holder.mProductQuantity.setText(String.valueOf(quantity));
    }

    @Override
    public int getItemCount() {
        return mGroceryItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mProductImage;
        private TextView mProductName;
        private TextView mSellingPrice;
        private TextView mGSTRate;
        private TextView mProductQuantity;
        private ImageView mAddQuantity;
        private ImageView mRemoveQuantity;
        private ImageView mProductDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mProductImage = itemView.findViewById(R.id.product_img);
            mProductName = itemView.findViewById(R.id.product_name);
            mSellingPrice = itemView.findViewById(R.id.selling_price);
            mGSTRate = itemView.findViewById(R.id.gst_rate);
            mProductQuantity = itemView.findViewById(R.id.product_qty);
            mAddQuantity = itemView.findViewById(R.id.add);
            mRemoveQuantity = itemView.findViewById(R.id.remove);
            mProductDelete = itemView.findViewById(R.id.product_del);

            mAddQuantity.setOnClickListener(this);
            mRemoveQuantity.setOnClickListener(this);
            mProductDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mSocietyAPI = SocietyClient.getClient(mContext)
                        .create(SocietyAPI.class);
                Items item = mGroceryItems.get(position);

                switch (view.getId()) {
                    case R.id.add:
                        int currQuantity =
                                Integer.parseInt(mProductQuantity.getText().toString()) + 1;

                        mProductQuantity.setText(String.valueOf(currQuantity));

                        updateCart(item, currQuantity, "Added to Cart");
                        break;
                    case R.id.remove:
                        currQuantity =
                            Integer.parseInt(mProductQuantity.getText().toString()) - 1;
                        if (currQuantity < 0)
                            Toast.makeText(mContext, "Quantity can't be less than 0",
                                    Toast.LENGTH_SHORT).show();
                        else
                            updateCart(item, currQuantity, "Removed from Cart");
                        break;
                    case R.id.product_del:
                        updateCart(item, 0, "Deleted from Cart");
                        break;
                }
            }
        }

        private void updateCart(Items item, int quantity, String statusOfCart) {
            SharedPreferences prefs = mContext.getSharedPreferences("cartQuantity",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();

            mSocietyAPI.addToCart(item.getItemCode(),
                    Integer.parseInt(mProductQuantity.getText().toString()))
                .enqueue(new Callback<Authentication>() {
                    @Override
                    public void onResponse(@NotNull Call<Authentication> call,
                                           @NotNull Response<Authentication> response) {
                        Timber.d(response.toString());
                        Authentication auth = response.body();
                        if (auth.getSuccessMsg().equals("true")) {
                            edit.putInt(item.getItemCode(), quantity);
                            edit.apply();

                            mProductQuantity.setText(String.valueOf(quantity));
                            Toast.makeText(mContext, statusOfCart, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Error: " + auth.getErrorCode(),
                                Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Authentication> call,
                                          @NotNull Throwable t) {
                        Timber.d(t);
                    }
                });
        }
    }
}
