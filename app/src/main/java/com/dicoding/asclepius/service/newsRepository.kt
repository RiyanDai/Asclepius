package com.dicoding.asclepius.service

import com.dicoding.asclepius.model.newsData
import java.io.IOException

class newsRepository {
    suspend fun getNews(country: String, apiKey: String): List<newsData> {
        val response = ApiConfig.newsApi.getNews(country, apiKey)
        if(response.isSuccessful) {
            val newsResponse = response.body()
            return newsResponse?.articles?: emptyList()
        }
        else {
            throw IOException("Error getting top headlines : ${response.code()}")
        }
    }
}