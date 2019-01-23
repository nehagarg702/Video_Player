package com.example.myapplication.model;

import android.content.Intent;

import com.example.myapplication.model.Constants;
import com.example.myapplication.model.Video;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.ApiInterface;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;

public class SignIn {

    public Call<ArrayList<Video>> getData()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        ApiInterface request = ApiClient.getClient(httpClient).create(ApiInterface.class);

        Call<ArrayList<Video>> call = request.getSearchResults(Constants.URL);
        return call;
    }

    public void IntenttoExo(Intent i, Video selectedData)
    {
        i.putExtra("title",selectedData.getTitle());
        i.putExtra("description",selectedData.getDescription());
        i.putExtra("url",selectedData.getUrl());
        i.putExtra("id",selectedData.getid());
    }


}
