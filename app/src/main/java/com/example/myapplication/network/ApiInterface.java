package com.example.myapplication.network;/*
used to get the remaining url part used for getting the data
 */

import com.example.myapplication.model.Video;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {



    @GET
    Call<ArrayList<Video>> getSearchResults(@Url String query);

}
