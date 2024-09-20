package com.example.redtop.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query

@Dao
interface PublicationDao{

    @Query("SELECT * FROM PublicationEntity")
    fun getAll(): List<PublicationEntity>

    @Query("SELECT * FROM PublicationEntity WHERE id == :id")
    fun get(id: Int): PublicationEntity?

    @Insert(onConflict = REPLACE)
    fun insert(publication: PublicationEntity): Long

    @Query("DELETE FROM PublicationEntity WHERE id == :id")
    fun delete(id: Int)
}