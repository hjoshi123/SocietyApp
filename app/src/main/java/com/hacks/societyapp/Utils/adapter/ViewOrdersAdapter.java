package com.hacks.societyapp.Utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hacks.societyapp.R;
import com.hacks.societyapp.activity.OrderDetailsActivity;
import com.hacks.societyapp.model.OrderResponse;

import java.util.ArrayList;

import timber.log.Timber;

public class ViewOrdersAdapter extends RecyclerView.Adapter<ViewOrdersAdapter.ViewHolder> {
    private ArrayList<OrderResponse> mTotalOrders;
    private Context mContext;

    public ViewOrdersAdapter(Context context, ArrayList<OrderResponse> orders) {
        this.mTotalOrders = orders;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.view_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderResponse order = mTotalOrders.get(position);

        holder.mStatus.setText(order.getStatus());
        holder.mTotalItems.setText(order.getNumItems().toString());
        holder.mOrderDate.setText(order.getOrderDate());
        holder.mTotalPrice.setText("₹ " + order.getTotalValue());
        holder.mOrderId.setText(order.getOrderId().toString());
    }

    @Override
    public int getItemCount() {
        return mTotalOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mStatus;
        private TextView mTotalPrice;
        private TextView mOrderDate;
        private TextView mTotalItems;
        private TextView mOrderId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mStatus = itemView.findViewById(R.id.status);
            mTotalPrice = itemView.findViewById(R.id.payableAmount);
            mOrderDate = itemView.findViewById(R.id.order_date);
            mTotalItems = itemView.findViewById(R.id.total_items);
            mOrderId = itemView.findViewById(R.id.orderId);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Timber.tag("Clicked").d("" + getAdapterPosition());
            Intent intent = new Intent(mContext, OrderDetailsActivity.class);
            intent.putExtra("position", getAdapterPosition());

            mContext.startActivity(intent);
        }
    }
}
