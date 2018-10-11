package com.tripkorea.on.ontripkorea.retrofit.client;

/**
 * Created by Edward Won on 2018-09-15.
 */

import com.tripkorea.on.ontripkorea.vo.user.Me;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddHeaderInterceptor implements Interceptor {


    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        int userIdx = Me.getInstance().getUserIdx();
        if (userIdx != 0) {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("User-Idx", String.valueOf(userIdx));
            return chain.proceed(builder.build());
        }
        return chain.proceed(chain.request());
    }
}
