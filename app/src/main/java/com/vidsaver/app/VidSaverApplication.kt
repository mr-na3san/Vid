package com.vidsaver.app

import android.app.Application
import com.vidsaver.app.data.network.NetworkModule
import com.vidsaver.app.data.repository.TikTokRepository
import com.vidsaver.app.data.repository.TikTokRepositoryImpl

class VidSaverApplication : Application() {

    lateinit var repository: TikTokRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val httpClient = NetworkModule.provideOkHttpClient()
        val api = NetworkModule.provideTikTokApiService(BuildConfig.EXTRACTION_API_BASE_URL, httpClient)
        repository = TikTokRepositoryImpl(api, httpClient, applicationContext)
    }
}
