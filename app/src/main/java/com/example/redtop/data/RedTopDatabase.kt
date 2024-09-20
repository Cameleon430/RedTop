package com.example.redtop.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PublicationEntity::class],
    version = 1
)

abstract class RedTopDatabase: RoomDatabase(){
    abstract fun providePublicationDao():PublicationDao
}