package com.daniilshevtsov.deduplication.feature.storage.data

import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.core.Reference
import com.daniilshevtsov.deduplication.feature.storage.domain.StorageRepository
import javax.inject.Inject

@ApplicationScope
class StorageRepositoryImpl @Inject constructor(
    private val storageApi: StorageApi
) : StorageRepository {

    override fun saveChunk(chunk: Chunk): Reference {
        return storageApi.saveChunkByValue(chunk = chunk)
    }

    override fun getByReference(reference: Reference): Chunk {
        return storageApi.getByReference(reference = reference)
    }

    override fun saveReference(reference: Reference) {
        storageApi.saveChunkByReference(reference = reference)
    }

    override fun readAsSequence(pageId:String): Sequence<SavedData> = storageApi.readAsSequence(pageId)

    override fun setCurrentPageId(pageId: String) {
        storageApi.setCurrentPageId(pageId)
    }
}