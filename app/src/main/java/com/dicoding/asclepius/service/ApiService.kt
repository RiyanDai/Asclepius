package com.dicoding.asclepius.service

import com.dicoding.asclepius.model.newsList
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines?q=cancer&category=health&language=en&apiKey=577aaf7efccc4605a0657718c7d95efe")
    suspend fun getNews(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String,
    ) : retrofit2.Response<newsList>
}