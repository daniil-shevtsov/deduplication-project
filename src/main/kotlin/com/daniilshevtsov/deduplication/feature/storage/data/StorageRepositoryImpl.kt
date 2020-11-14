package com.daniilshevtsov.deduplication.feature.storage.data

import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.core.Reference
import com.daniilshevtsov.deduplication.feature.storage.domain.StorageRepository
import javax.inject.Inject

@ApplicationScope
class StorageRepositoryImpl @Inject constructor(

) : StorageRepository {

    private val storage = mutableListOf<SavedData>()

    override fun saveChunk(chunk: Chunk): Reference {
        val savedValue = SavedData.Value(chunk = chunk)
        storage.add(savedValue)

        return Reference(index = storage.indexOf(savedValue))
    }

    override fun getByReference(reference: Reference): Chunk {
        return (storage[reference.index] as SavedData.Value).chunk
    }

    override fun saveReference(reference: Reference) {
        storage.add(SavedData.TableReference(reference = reference))
    }

    override fun readAsSequence(): Sequence<SavedData> = storage.asSequence()

}