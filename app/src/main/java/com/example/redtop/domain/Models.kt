package com.example.redtop.domain

data class Publication(
    val id: Int = 0,
    val author: String,
    val title: String,
    val timeStamp: String,
    val media: String,
    val commentsCount: String
)