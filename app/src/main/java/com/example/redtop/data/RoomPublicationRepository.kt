package com.example.redtop.data

import com.example.redtop.domain.Publication
import com.example.redtop.domain.PublicationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomPublicationRepository(
    private val dao: PublicationDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
): PublicationRepository {

    override suspend fun getAll(): List<Publication> {
        return withContext(dispatcher) {
            dao.getAll().map {
                it.toModel()
            }
        }
    }

    override suspend fun get(id: Int): Publication? {
        return withContext(dispatcher) {
            dao.get(id)?.toModel()
        }
    }

    override suspend fun add(publication: Publication) {
        withContext(dispatcher) {
            val updatedPublication = publication.copy(id = 0)
            dao.insert(updatedPublication.toEntity())
        }
    }

    override suspend fun update(publication: Publication) {
        withContext(dispatcher) {
            dao.insert(publication.toEntity())
        }
    }

    override suspend fun delete(publication: Publication) {
        withContext(dispatcher) {
            dao.delete(publication.id)
        }
    }

    private fun PublicationEntity.toModel(): Publication{
        return Publication(
            id = id,
            author = author,
            title = title,
            selftext = selftext,
            timeStamp = timeStamp,
            media = media,
            commentsCount = commentsCount
        )
    }

    private fun Publication.toEntity(): PublicationEntity{
        return PublicationEntity(
            id = id,
            author = author,
            title = title,
            selftext = selftext,
            timeStamp = timeStamp,
            media = media,
            commentsCount = commentsCount
        )
    }

}