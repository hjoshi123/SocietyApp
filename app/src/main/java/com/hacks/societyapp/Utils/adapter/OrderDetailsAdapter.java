package com.hacks.societyapp.Utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hacks.societyapp.R;
import com.hacks.societyapp.model.Items;
import com.hacks.societyapp.model.OrderResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private ArrayList<OrderResponse.Item> mItems;

    public OrderDetailsAdapter(ArrayList<OrderResponse.Item> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.view_order_details,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderResponse.Item item = mItems.get(position);

        holder.mProductName.setText(item.getItemDescription());
        holder.mSellingPrice.setText("₹ " + item.getRate());
        holder.mGSTRate.setText("  " +item.getGstRate() + " % GST");
        holder.mProductQuantity.setText(item.getQuantity().toString());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mProductImage;
        private TextView mProductName;
        private TextView mSellingPrice;
        private TextView mGSTRate;
        private TextView mProductQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mProductImage = itemView.findViewById(R.id.product_img);
            mProductName = itemView.findViewById(R.id.product_name);
            mSellingPrice = itemView.findViewById(R.id.selling_price);
            mGSTRate = itemView.findViewById(R.id.gst_rate);
            mProductQuantity = itemView.findViewById(R.id.quantity);
        }
    }
}
