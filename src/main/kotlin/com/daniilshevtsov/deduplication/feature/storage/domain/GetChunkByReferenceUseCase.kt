package com.daniilshevtsov.deduplication.feature.storage.domain

import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.indextable.domain.IndexTableRepository
import javax.inject.Inject

class GetChunkByReferenceUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val tableRepository: IndexTableRepository
) {
    operator fun invoke(referenceId: Int): Chunk {
        val reference = tableRepository.get(key = referenceId)
        val chunk = storageRepository.getByReference(reference = reference!!)
        return chunk
    }
}