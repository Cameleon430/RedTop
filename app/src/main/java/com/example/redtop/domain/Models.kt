package com.example.redtop.domain

data class Publication(
    val id: Int = 0,
    val author: String,
    val timeStamp: Int = 0,
    val image: String,
    val commentsCount: Int = 0
)