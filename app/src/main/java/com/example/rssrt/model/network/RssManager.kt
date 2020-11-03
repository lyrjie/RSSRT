package com.example.rssrt.model.network

import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object RssManager {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    val rssService: RssInterface = retrofit.create(RssInterface::class.java)
}