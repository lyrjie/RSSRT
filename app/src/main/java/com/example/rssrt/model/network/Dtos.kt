package com.example.rssrt.model.network

import org.simpleframework.xml.*

@Root(name = "rss", strict = false)
data class RssFeed @JvmOverloads constructor(

    @field:Element(name = "channel")
    var channel: RssChannel? = null

)

@Root(name = "channel", strict = false)
data class RssChannel @JvmOverloads constructor(
    @field:ElementList(name = "items", inline = true)
    var items: List<RssItem> = mutableListOf(),

    @field:Path("title")
    @field:Text(required = false)
    var title: String? = null,

    @field:Element(name = "description")
    var description: String? = null
)

@Root(name = "item", strict = false)
data class RssItem @JvmOverloads constructor(

    @field:Path("title")
    @field:Text(required = false)
    var title: String? = null,

    @field:Path("link")
    @field:Text(required = false)
    var link: String? = null,

    @field:Element(name = "description")
    var description: String? = null,

    @field:Element(name = "pubDate")
    var publicationDate: String? = null

)