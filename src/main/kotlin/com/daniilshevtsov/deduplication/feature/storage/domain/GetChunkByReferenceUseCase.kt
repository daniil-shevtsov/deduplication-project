package com.daniilshevtsov.deduplication.feature.storage.domain

import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.indextable.domain.IndexTableRepository
import javax.inject.Inject

object Counter {
    var count = 0
}

class GetChunkByReferenceUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val tableRepository: IndexTableRepository
) {
    operator fun invoke(referenceId: String): Chunk {
        val reference = tableRepository.get(key = referenceId)
        val chunk = if (reference == null) {
            Counter.count++
            Chunk(value = "����".toByteArray().toList())
        } else {
            storageRepository.getByReference(reference = reference)
        }

        return chunk
    }
}