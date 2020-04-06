package com.hacks.societyapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.hacks.societyapp.model.OrderResponse;
import com.hacks.societyapp.retrofitapi.SocietyAPI;
import com.hacks.societyapp.retrofitclient.SocietyClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class CheckOrderStatusWorker extends Worker {
    private SocietyAPI mSocietyAPI;
    private Context mContext;

    public CheckOrderStatusWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;

        mSocietyAPI = SocietyClient.getClient(context)
            .create(SocietyAPI.class);
    }

    @NonNull
    @Override
    public Result doWork() {
        final int[] flag = {1};
        mSocietyAPI.getOrder()
            .enqueue(new Callback<ArrayList<OrderResponse>>() {
                @Override
                public void onResponse(@NotNull Call<ArrayList<OrderResponse>> call,
                                       @NotNull Response<ArrayList<OrderResponse>> response) {
                    if (response.isSuccessful()) {
                        SharedPreferences prefs = mContext.getSharedPreferences("notifs",
                                Context.MODE_PRIVATE);

                        ArrayList<OrderResponse> orders = response.body();
                        for (OrderResponse order: orders) {
                            if (order.getStatus().toLowerCase().equals("completed") &&
                                    (prefs.getInt(order.getOrderId().toString(), 0) != 1)) {

                                Timber.tag("Notification call").d(order.getStatus());
                                NotificationUtils.reminderUserAboutOrderStatus(mContext,
                                    order.getOrderId(),
                                    "completed. Please collect your order",
                                    order.getOrderDate(),
                                    order.getTotalValue());
                            } else if (order.getStatus().toLowerCase().equals("cancelled") &&
                                (prefs.getInt(order.getOrderId().toString(), 0) != 1)) {

                                NotificationUtils.reminderUserAboutOrderStatus(mContext,
                                    order.getOrderId(),
                                    "cancelled. Sorry for the inconvenience caused",
                                    order.getOrderDate(),
                                    order.getTotalValue());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<OrderResponse>> call, Throwable t) {
                    flag[0] = 0;
                    Timber.tag("Worker Failure").d(t);
                }
            });
        if (flag[0] == 0)
            return Result.failure();
        else
            return Result.success();
    }
}
