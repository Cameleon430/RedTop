package com.example.redtop.domain

interface PublicationRepository {
    suspend fun getAll(): List<Publication>

    suspend fun get(id: Int): Publication?

    suspend fun  add(publication: Publication)

    suspend fun update(publication: Publication)

    suspend fun delete(publication: Publication)
}