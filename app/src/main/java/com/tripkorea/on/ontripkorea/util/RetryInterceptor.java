package com.tripkorea.on.ontripkorea.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Edward Won on 2018-06-11.
 */

public class RetryInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        int tryCount = 0;
        int maxLimit = 3;
        while (!response.isSuccessful() && tryCount < maxLimit) {
            Log.d("interceptor", "Request failed - " + tryCount);
            tryCount++;
            response = chain.proceed(request);
            if(tryCount == 3){
                Context application = MyApplication.getContext();
                Log.e("interceptor", "문제발생");
            }
        }
        return response;
    }
}
