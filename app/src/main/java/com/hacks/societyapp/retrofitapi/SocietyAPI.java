package com.hacks.societyapp.retrofitapi;

import com.hacks.societyapp.model.Authentication;
import com.hacks.societyapp.model.CartItems;
import com.hacks.societyapp.model.Items;
import com.hacks.societyapp.model.OrderResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SocietyAPI {
    @FormUrlEncoded
    @POST("register")
    Call<Authentication> registerUser(@Field("employee_number") String emplNumber,
                                      @Field("email_id") String emailId,
                                      @Field("password") String password);

    @FormUrlEncoded
    @POST("login")
    Call<Authentication> loginUser(@Field("email_id") String emailId,
                                   @Field("password") String password);

    @POST("getItems")
    Call<ArrayList<Items>> getItems();

    @FormUrlEncoded
    @POST("addCart")
    Call<Authentication> addToCart(@Field("item_code") String itemCode,
                                 @Field("quantity") Integer quantity);

    @GET("getCart")
    Call<ArrayList<CartItems>> getCart();

    @POST("placeOrder")
    Call<Authentication> placeOrder();

    @GET("getOrder")
    Call<ArrayList<OrderResponse>> getOrder();
}
