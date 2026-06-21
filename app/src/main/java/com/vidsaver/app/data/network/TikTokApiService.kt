package com.vidsaver.app.data.network

import com.vidsaver.app.data.model.TikTokApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TikTokApiService {

    @GET("api/")
    suspend fun fetchVideoInfo(@Query("url") videoUrl: String): TikTokApiResponse
}
