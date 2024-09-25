package com.example.redtop.presenter.base

data class PublicationViewState(
    val id:Int,
    val author:String,
    val title: String,
    val timeStamp: String,
    val media: String,
    val commentsCount: String
)