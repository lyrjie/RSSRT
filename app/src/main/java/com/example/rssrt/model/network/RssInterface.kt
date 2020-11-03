package com.example.rssrt.model.network

import retrofit2.http.GET
import retrofit2.http.Url

interface RssInterface {

    @GET
    suspend fun getRss(@Url url: String): RssFeed

}

