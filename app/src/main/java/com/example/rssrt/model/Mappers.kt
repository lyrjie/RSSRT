package com.example.rssrt.model

import com.example.rssrt.model.database.Channel
import com.example.rssrt.model.database.Item
import com.example.rssrt.model.network.RssChannel
import com.example.rssrt.model.network.RssItem
import java.text.SimpleDateFormat
import java.util.*

val rfc822DateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)

fun RssChannel.toDomain(url: String): Channel {
    return Channel(
        url = url,
        title = title.orEmpty(),
        description = description.orEmpty()
    )
}

fun RssItem.toDomain(channelId: Long): Item {
    return Item(
        channelId = channelId,
        title = title,
        link = link,
        description = description,
        publicationDate = publicationDate?.let { rfc822DateFormat.parse(it) }
    )
}