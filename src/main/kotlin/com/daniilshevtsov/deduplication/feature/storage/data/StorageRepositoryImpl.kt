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

//    private val storage = mutableListOf<SavedData>()

    override fun saveChunk(chunk: Chunk): Reference {
        return storageApi.saveChunkByValue(chunk = chunk)
    }

    override fun getByReference(reference: Reference): Chunk {
        return storageApi.getByReference(reference = reference)
    }

    override fun saveReference(reference: Reference) {
        storageApi.saveChunkByReference(reference = reference)
    }

    override fun readAsSequence(): Sequence<SavedData> = storageApi.readAsSequence().map { line ->
        when {
            line.startsWith("reference:") -> {
                SavedData.TableReference(referenceId = line.substringAfter("reference:").toInt())
            }
            line.startsWith("value:") -> {
                SavedData.Value(chunk = Chunk(value = line.substringAfter("value:").toByteArray().toList()))
            }
            else -> throw IllegalStateException("not value nor reference")
        }

    }

}