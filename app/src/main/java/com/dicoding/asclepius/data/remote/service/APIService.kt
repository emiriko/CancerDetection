package com.dicoding.asclepius.data.remote.service

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = BuildConfig.CANCER_API_KEY,
        @Query("category") category: String = "health",
    ): Response
}