package com.example.rssrt.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rssrt.model.database.Channel
import com.example.rssrt.model.database.Item
import com.example.rssrt.model.database.NO_ROWS_AFFECTED_RESULT
import com.example.rssrt.model.database.RssDatabase
import com.example.rssrt.model.network.RssFeed
import com.example.rssrt.model.network.RssManager
import java.lang.Exception

class RssRepository(private val database: RssDatabase) {

    val channels = database.rssDao.getLiveChannels()

    suspend fun refreshChannel(channelId: Long) {
        val channel = database.rssDao.getChannelById(channelId)
        channel?.let {
            fetchChannel(channel.url)?.let { channelData ->
                updateChannel(channelData, channelId, channel.url)
                persistChannelItems(channelId, channelData)
            }
        }
    }

    suspend fun refreshAllChannels(progress: MutableLiveData<Int>) {
        val channels = database.rssDao.getChannels()
        val channelCount = channels.size
        channels.forEachIndexed { index, channel ->
            progress.postValue((index + 1) * 100 / channelCount)
            refreshChannel(channel.id)
        }
    }

    private suspend fun updateChannel(
        channelData: RssFeed,
        channelId: Long,
        url: String
    ) {
        channelData.channel?.toDomain(url)?.let {
            it.id = channelId
            database.rssDao.updateChannel(it)
        }
    }

    suspend fun createChannel(rssUrl: String): ChannelCreationResult {
        val channelData = fetchChannel(rssUrl)
        val channel = channelData?.channel?.toDomain(rssUrl) ?: return ChannelCreationResult.NOT_RSS
        val channelId = database.rssDao.addChannel(channel)
        persistChannelItems(channelId, channelData)
        return if (channelId == NO_ROWS_AFFECTED_RESULT) ChannelCreationResult.ALREADY_PRESENT else ChannelCreationResult.CREATED
    }

    suspend fun initialPopulate() {
        database.rssDao.addChannels(
            listOf(
                Channel("https://habr.com/ru/rss/hubs/all/", "", ""),
                Channel("http://www.thecrazyprogrammer.com/feed", "", ""),
                Channel("https://www.sitepoint.com/feed/", "", "")
            )
        )
    }

    private suspend fun persistChannelItems(channelId: Long, channelData: RssFeed) {
        val items = channelData.channel?.items?.map { it.toDomain(channelId) }
            ?.toMutableList()
        items?.let {
            database.rssDao.removeItemsByChannelId(channelId)
            database.rssDao.addItems(it)
        }
    }

    private suspend fun fetchChannel(rssUrl: String): RssFeed? {
        return try {
            RssManager.rssService.getRss(rssUrl)
        } catch (exception: Exception) {
            null
        }
    }

    suspend fun removeChannel(channel: Channel) {
        database.rssDao.removeChannel(channel)
    }

    fun getChannelItems(channelId: Long): LiveData<List<Item>> {
        return database.rssDao.getChannelItems(channelId)
    }

}

enum class ChannelCreationResult {
    CREATED, ALREADY_PRESENT, NOT_RSS
}


