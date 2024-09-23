package com.example.redtop.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.redtop.data.*
import com.example.redtop.domain.PublicationRepository

object ServiceLocator {

    private var publicationRepository: PublicationRepository? = null
    private lateinit var database: RedTopDatabase

    fun initializeDatabase(context: Context){
        database = Room.databaseBuilder(context, RedTopDatabase::class.java, "red-top-database").build()
    }

    fun providePublicationRepository(): PublicationRepository{
        if (publicationRepository == null){
            val dao = providePublicationDao()
            publicationRepository = RoomPublicationRepository(dao)
        }
        return publicationRepository!!
    }

    fun providePublicationDao(): PublicationDao{
        return database.providePublicationDao()
    }
}