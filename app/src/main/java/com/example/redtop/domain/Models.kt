package com.example.redtop.domain

data class Publication(
    val id: Int = 0,
    val author: String,
    val title: String,
    val timeStamp: Int,
    val image: String,
    val commentsCount: String
)