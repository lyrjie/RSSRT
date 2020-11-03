package com.example.rssrt.model.database

import android.content.Context
import androidx.room.*

const val NO_ROWS_AFFECTED_RESULT = -1L

@Database(entities = [Channel::class, Item::class], version = 3)
@TypeConverters(AppConverters::class)
abstract class RssDatabase : RoomDatabase() {
    abstract val rssDao: RssDao
}

private lateinit var INSTANCE: RssDatabase

// In real life this is handled by DI, but this is out of scope of the test task
fun getDatabase(context: Context): RssDatabase {
    synchronized(RssDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                RssDatabase::class.java,
                "rss-database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}