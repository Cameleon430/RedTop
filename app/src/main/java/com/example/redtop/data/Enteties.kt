package com.example.redtop.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PublicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "timeStamp") val timeStamp: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "commentsCount") val commentsCount: String
)