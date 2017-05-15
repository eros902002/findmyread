package com.erostech.findmyread.api;

import android.app.Application;
import android.content.Context;

import com.erostech.findmyread.Constants;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by erosgarciaponte on 15/05/2017.
 */

public class BookSearchService {
    private BookSearchApi api;

    private static BookSearchService sInstance;

    private BookSearchService() {
        api = getRetrofit(getGson(), getOkhttpClient()).create(BookSearchApi.class);
    }

    public static BookSearchService getInstance() {
        if (sInstance == null) {
            sInstance = new BookSearchService();
        }
        return sInstance;
    }

    private Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    private OkHttpClient getOkhttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return client.build();
    }

    private Retrofit getRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.BASE_API_URL)
                .client(okHttpClient)
                .build();
    }

    public BookSearchApi getApi() {
        return api;
    }
}
