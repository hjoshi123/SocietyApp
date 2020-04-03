package com.hacks.societyapp.retrofitclient;

import android.content.Context;

import com.hacks.societyapp.Utils.AddCookiesInterceptor;
import com.hacks.societyapp.Utils.ReceivedCookiesInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SocietyClient {
    private final static String BASE_URL = "http://13.59.78.188:5000/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        httpClient.interceptors().add(new AddCookiesInterceptor(context));
        httpClient.interceptors().add(new ReceivedCookiesInterceptor(context));

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retrofit;
    }
}
