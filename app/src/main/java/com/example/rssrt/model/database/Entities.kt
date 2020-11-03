package com.example.rssrt.model.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(indices = [Index(value = ["url"], unique = true)])
data class Channel(
    val url: String,

    val title: String,

    val description: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
)

@Entity
@Parcelize
data class Item(
    val channelId: Long,

    val title: String?,

    val link: String?,

    val description: String?,

    val publicationDate: Date?,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
) : Parcelable