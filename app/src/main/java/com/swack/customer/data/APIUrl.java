package com.swack.customer.data;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUrl {
    public static final String BASE_URL = "http://swack.in/swack/UserApi/";
    public static final String KEY = "123456789";
    //public static final String BASE_IMAGE = "http://swack.in/swack/";
    public static final String BASE_IMAGE = "http://operandtechnologies.com/swack/GarageUserApi/UploadDocs/SliderImages/";
    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
