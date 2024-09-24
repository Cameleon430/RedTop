package com.example.redtop.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PublicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "timeStamp") val timeStamp: String,
    @ColumnInfo(name = "media") val media: String,
    @ColumnInfo(name = "commentsCount") val commentsCount: String
)