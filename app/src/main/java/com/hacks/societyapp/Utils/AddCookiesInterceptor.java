package com.hacks.societyapp.Utils;

import android.content.Context;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class AddCookiesInterceptor implements Interceptor {
    private Context mContext;

    public AddCookiesInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = CookieUtils.getCookies(mContext);
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Timber.tag("OkHttp").v("Adding Header: " + cookie);
        }
        return chain.proceed(builder.build());
    }
}