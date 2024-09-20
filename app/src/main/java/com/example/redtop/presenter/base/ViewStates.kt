package com.example.redtop.presenter.base

data class PublicationViewState(
    val id:Int,
    val author:String,
    val title: String,
    val timeStamp: Int,
    val image: String,
    val commentsCount: String
)