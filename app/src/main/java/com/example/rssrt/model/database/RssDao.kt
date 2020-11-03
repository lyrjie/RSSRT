package com.example.rssrt.model.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RssDao {

    @Query("select * from channel")
    fun getLiveChannels(): LiveData<List<Channel>>

    @Query("select * from channel")
    suspend fun getChannels(): List<Channel>

    @Query("select * from channel where id = :id")
    suspend fun getChannelById(id: Long): Channel?

    @Query("select * from item where channelId = :channelId")
    fun getChannelItems(channelId: Long): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addChannel(channel: Channel): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addChannels(channels: List<Channel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItems(items: List<Item>)

    @Query("delete from item where channelId = :channelId")
    suspend fun removeItemsByChannelId(channelId: Long)

    @Delete
    suspend fun removeChannel(channel: Channel)

    @Update
    suspend fun updateChannel(channel: Channel)

}