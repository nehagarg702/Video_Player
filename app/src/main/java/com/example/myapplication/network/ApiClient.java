package com.example.myapplication.network;/*
use the Retrofit to get data from url and use okhttp to build the request
 */

import com.example.myapplication.model.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/****
 * Class used to get the data from url by using retrofit and okhttp
 */
public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(OkHttpClient.Builder httpClient) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //<-- convert the JSON to JAVA objects
                .client(httpClient.build())                         //<-- Builds the request with OkHttp
                .build();
        }
        return retrofit;
    }
}
