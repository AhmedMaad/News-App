package com.maad.newsapp.main

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCallable {

    @GET("/v2/top-headlines?apiKey=d1fe0379ee014daeb144ecc2c7196efa")
    fun getNews(
        @Query("category") cat: String,
        @Query("country") code: String
    ): Call<News>

}