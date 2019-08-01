package com.example.newsapp.Network

import com.example.newsapp.Data.baseJsonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ArticlesWebService {

        @GET("/search")
        fun getArticles(
            @Query("show-tags") tags: String = "contributor",
            @Query("api-key") api:String="test",
            @Query("show-fields") fields:String="thumbnail",
            @Query("page-size") page:String="20",
            @Query("order-by") order:String="newest"
        ): Call<baseJsonResponse>

    }